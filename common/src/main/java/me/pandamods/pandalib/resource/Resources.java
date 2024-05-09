package me.pandamods.pandalib.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.core.utils.typeadapter.Matrix4fTypeAdapter;
import me.pandamods.pandalib.core.utils.typeadapter.QuaternionfTypeAdapter;
import me.pandamods.pandalib.core.utils.typeadapter.Vector2fTypeAdapter;
import me.pandamods.pandalib.core.utils.typeadapter.Vector3fTypeAdapter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.*;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.regex.Pattern;

public class Resources implements PreparableReloadListener {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final String SUPPORTED_ARMATURE_VERSION = "0.2";
	private static final String SUPPORTED_ANIMATION_VERSION = "0.2";

	public static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(Vector2fc.class, new Vector2fTypeAdapter())
			.registerTypeAdapter(Vector3fc.class, new Vector3fTypeAdapter())
			.registerTypeAdapter(Quaternionfc.class, new QuaternionfTypeAdapter())
			.registerTypeAdapter(Matrix4fc.class, new Matrix4fTypeAdapter())
			.create();

	private static final List<ResourceLocation> missingResources = new ObjectArrayList<>();

	public Map<ResourceLocation, Mesh> meshes = new Object2ObjectOpenHashMap<>();
	public Map<ResourceLocation, ArmatureData> armatures = new Object2ObjectOpenHashMap<>();
	public Map<ResourceLocation, AnimationData> animations = new Object2ObjectOpenHashMap<>();

	public static Mesh getMesh(ResourceLocation resourceLocation) {
		Mesh meshData = ExtraDetails.resources.meshes.get(resourceLocation);
		if (meshData == null) {
			if (missingResources.contains(resourceLocation))
				missingResources.add(resourceLocation);
			else
				LOGGER.error("Resource '{}' is missing", resourceLocation.toString());
		}
		return meshData;
	}

	@Override
	public @NotNull CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager,
												   ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler,
												   Executor backgroundExecutor, Executor gameExecutor) {
		Map<ResourceLocation, Mesh> meshes = new Object2ObjectOpenHashMap<>();
		Map<ResourceLocation, ArmatureData> armatures = new Object2ObjectOpenHashMap<>();
		Map<ResourceLocation, AnimationData> animations = new Object2ObjectOpenHashMap<>();
		return CompletableFuture.allOf(
 				loadMesh("pandalib/meshes", backgroundExecutor, resourceManager, meshes::put),
				load("pandalib/armatures", SUPPORTED_ARMATURE_VERSION, ArmatureData.class, backgroundExecutor,
						resourceManager, armatures::put),
				load("pandalib/animations", SUPPORTED_ANIMATION_VERSION, AnimationData.class, backgroundExecutor,
						resourceManager, animations::put)
		).thenCompose(preparationBarrier::wait)
				.thenAcceptAsync(unused -> this.meshes = meshes, gameExecutor)
				.thenAcceptAsync(unused -> this.armatures = armatures, gameExecutor)
				.thenAcceptAsync(unused -> this.animations = animations, gameExecutor);
	}
	private CompletableFuture<Void> loadMesh(String directory, Executor executor,
										  ResourceManager resourceManager, BiConsumer<ResourceLocation, Mesh> map) {
		return CompletableFuture.supplyAsync(() -> resourceManager.listResources(directory, resource -> true), executor)
				.thenApplyAsync(resources -> {
					Map<ResourceLocation, CompletableFuture<Mesh>> tasks = new Object2ObjectOpenHashMap<>();

					for (ResourceLocation resource : resources.keySet()) {
						try (InputStream inputStream = resourceManager.getResourceOrThrow(resource).open()) {
							byte[] bytes = inputStream.readAllBytes();
							ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
							buffer.put(bytes);
							buffer.flip();
							AIScene aiScene = Assimp.aiImportFileFromMemory(buffer,
									Assimp.aiProcess_Triangulate | Assimp.aiProcess_JoinIdenticalVertices, "");
							if (aiScene != null)
								tasks.put(resource, CompletableFuture.supplyAsync(() -> new Mesh(aiScene), executor));
						}
						catch (Exception e) {
							LOGGER.error("Couldn't load '{}'", resource.toString(), e);
							throw new RuntimeException(new FileNotFoundException(resource.toString()));
						}
					}
					return tasks;
				}, executor).thenAcceptAsync(resource -> {
					for (Map.Entry<ResourceLocation, CompletableFuture<Mesh>> entry : resource.entrySet()) {
						map.accept(entry.getKey(), entry.getValue().join());
					}
				}, executor);
	}

	private <C> CompletableFuture<Void> load(String directory, String formatVersion, Class<C> resourceDataClass,
													Executor executor, ResourceManager resourceManager,
													BiConsumer<ResourceLocation, C> map) {
		return CompletableFuture.supplyAsync(() ->
				resourceManager.listResources(directory, resource ->
						resource.getPath().endsWith(".json")), executor)
				.thenApplyAsync(resources -> {
					Map<ResourceLocation, CompletableFuture<C>> tasks = new Object2ObjectOpenHashMap<>();

					for (ResourceLocation resource : resources.keySet()) {
						JsonObject json = loadFile(resource, resourceManager);
						if (!json.has("format_version")) {
							LOGGER.error("'{}' does not have a format version",
									resource);
						} else if (!json.get("format_version").getAsString().equals(formatVersion)) {
							LOGGER.error("format version '{}' of '{}' is not supported",
									json.get("format_version").getAsString(), resource);
						} else {
							C file = GSON.fromJson(json, resourceDataClass);
							tasks.put(resource, CompletableFuture.supplyAsync(() -> file, executor));
						}
					}

					return tasks;
				}, executor).thenAcceptAsync(resource -> {
					for (Map.Entry<ResourceLocation, CompletableFuture<C>> entry : resource.entrySet()) {
						map.accept(entry.getKey(), entry.getValue().join());
					}
				}, executor);
	}

	public JsonObject loadFile(ResourceLocation location, ResourceManager manager) {
		return GSON.fromJson(getFileContents(location, manager), JsonObject.class);
	}

	public String getFileContents(ResourceLocation location, ResourceManager manager) {
		try (InputStream inputStream = manager.getResourceOrThrow(location).open()) {
			return IOUtils.toString(inputStream, Charset.defaultCharset());
		}
		catch (Exception e) {
			LOGGER.error("Couldn't load '" + location + "'", e);
			throw new RuntimeException(new FileNotFoundException(location.toString()));
		}
	}
}

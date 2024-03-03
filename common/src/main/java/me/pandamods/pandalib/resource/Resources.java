package me.pandamods.pandalib.resource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.utils.gsonadapter.Vector2fTypeAdapter;
import me.pandamods.pandalib.utils.gsonadapter.Vector3fTypeAdapter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.io.IOUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

public class Resources implements PreparableReloadListener {
	private static final String SUPPORTED_MESH_VERSION = "0.3";
	private static final String SUPPORTED_ARMATURE_VERSION = "0.1";

	public static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(Vector2f.class, new Vector2fTypeAdapter())
			.registerTypeAdapter(Vector3f.class, new Vector3fTypeAdapter())
			.create();

	public Map<ResourceLocation, MeshData> meshes = new HashMap<>();
	public Map<ResourceLocation, ArmatureData> armatures = new HashMap<>();

	@Override
	public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager,
										  ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler,
										  Executor backgroundExecutor, Executor gameExecutor) {
		Map<ResourceLocation, MeshData> meshes = new Object2ObjectOpenHashMap<>();
		Map<ResourceLocation, ArmatureData> armatures = new Object2ObjectOpenHashMap<>();
		return CompletableFuture.allOf(
				load("pandalib/meshes", SUPPORTED_MESH_VERSION, MeshData.class, backgroundExecutor,
						resourceManager, meshes::put),
				load("pandalib/armatures", SUPPORTED_ARMATURE_VERSION, ArmatureData.class, backgroundExecutor,
						resourceManager, armatures::put)
		).thenCompose(preparationBarrier::wait)
				.thenAcceptAsync(unused -> this.meshes = meshes, gameExecutor)
				.thenAcceptAsync(unused -> this.armatures = armatures, gameExecutor);
	}

	private <C> CompletableFuture<Void> load(String directory, String formatVersion, Class<C> resourceDataClass,
													Executor executor, ResourceManager resourceManager,
													BiConsumer<ResourceLocation, C> map) {
		return CompletableFuture.supplyAsync(() ->
				resourceManager.listResources(directory, resource ->
						resource.getPath().endsWith(".json")), executor).thenApplyAsync(resources -> {
			Map<ResourceLocation, CompletableFuture<C>> tasks = new Object2ObjectOpenHashMap<>();

			for (ResourceLocation resource : resources.keySet()) {
				JsonObject json = loadFile(resource, resourceManager);
				if (!json.has("format_version") || !json.get("format_version").getAsString().equals(formatVersion)) {
					ExtraDetails.LOGGER.error("format version '{}' of '{}' is not supported",
							json.get("format_version").getAsString(), resource);
					continue;
				}

				C file = GSON.fromJson(json, resourceDataClass);
				tasks.put(resource, CompletableFuture.supplyAsync(() -> file, executor));
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
			ExtraDetails.LOGGER.error("Couldn't load '" + location + "'", e);

			throw new RuntimeException(new FileNotFoundException(location.toString()));
		}
	}
}

package me.pandamods.pandalib.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.utils.gsonadapter.Vector3fTypeAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.io.IOUtils;
import org.joml.Vector3f;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;

public class Resources {
	private static final String SUPPORTED_MESH_VERSION = "0.2";
	private static final String SUPPORTED_ANIMATION_VERSION = "0.1";

	public static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(Vector3f.class, new Vector3fTypeAdapter())
			.create();

	public static Map<ResourceLocation, Mesh> MESHES = new HashMap<>();
	public static Map<ResourceLocation, RawAnimation> ANIMATIONS = new HashMap<>();

	public static void registerReloadListener() {
		Minecraft mc = Minecraft.getInstance();
		if (!(mc.getResourceManager() instanceof ReloadableResourceManager resourceManager))
			throw new RuntimeException("PandaLib was initialized too early!");

		resourceManager.registerReloadListener(Resources::reload);
	}

	public static CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager manager,
														ProfilerFiller profilerFiller, ProfilerFiller profilerFiller1, Executor executor, Executor executor1) {
		Map<ResourceLocation, Mesh> meshes = new Object2ObjectOpenHashMap<>();
		Map<ResourceLocation, RawAnimation> animations = new Object2ObjectOpenHashMap<>();

		return CompletableFuture.allOf(loadMeshes(executor, manager, meshes::put), loadAnimations(executor, manager, animations::put))
				.thenCompose(preparationBarrier::wait)
				.thenAcceptAsync(unused -> Resources.MESHES = meshes, executor1)
				.thenAcceptAsync(unused -> Resources.ANIMATIONS = animations, executor1);
	}

	private static CompletableFuture<Void> loadMeshes(Executor executor, ResourceManager resourceManager,
														  BiConsumer<ResourceLocation, Mesh> map) {
		return CompletableFuture.supplyAsync(() ->
						resourceManager.listResources("pandalib/meshes", resource ->
								resource.getPath().endsWith(".json")), executor).thenApplyAsync(resources -> {
									Map<ResourceLocation, CompletableFuture<Mesh>> tasks = new Object2ObjectOpenHashMap<>();

									for (ResourceLocation resource : resources.keySet()) {
										Mesh mesh = loadMeshFiles(resource, resourceManager);
										if (mesh.format_version() != null && mesh.format_version().equals(SUPPORTED_MESH_VERSION))
											tasks.put(resource, CompletableFuture.supplyAsync(() -> mesh, executor));
										else
											PandaLib.LOGGER.error("Mesh format version " + mesh.format_version() + " is not supported");
									}

									return tasks;
		}, executor).thenAcceptAsync(resource -> {
			for (Map.Entry<ResourceLocation, CompletableFuture<Mesh>> entry : resource.entrySet()) {
						map.accept(entry.getKey(), entry.getValue().join());
					}
		}, executor);
	}

	public static Mesh loadMeshFiles(ResourceLocation location, ResourceManager manager) {
		return GSON.fromJson(loadFile(location, manager), Mesh.class);
	}

	private static CompletableFuture<Void> loadAnimations(Executor executor, ResourceManager resourceManager,
														  BiConsumer<ResourceLocation, RawAnimation> map) {
		return CompletableFuture.supplyAsync(() ->
				resourceManager.listResources("pandalib/animations", resource ->
						resource.getPath().endsWith(".json")), executor).thenApplyAsync(resources -> {
			Map<ResourceLocation, CompletableFuture<RawAnimation>> tasks = new Object2ObjectOpenHashMap<>();

			for (ResourceLocation resource : resources.keySet()) {
				RawAnimation rawAnimation = loadAnimationFiles(resource, resourceManager);
				if (rawAnimation.format_version() != null && rawAnimation.format_version().equals(SUPPORTED_ANIMATION_VERSION))
					tasks.put(resource, CompletableFuture.supplyAsync(() -> rawAnimation, executor));
				else
					PandaLib.LOGGER.error("Animation format version " + rawAnimation.format_version() + " is not supported");
			}

			return tasks;
		}, executor).thenAcceptAsync(resource -> {
			for (Map.Entry<ResourceLocation, CompletableFuture<RawAnimation>> entry : resource.entrySet()) {
				map.accept(entry.getKey(), entry.getValue().join());
			}
		}, executor);
	}

	public static RawAnimation loadAnimationFiles(ResourceLocation location, ResourceManager manager) {
		return GSON.fromJson(loadFile(location, manager), RawAnimation.class);
	}

	public static JsonObject loadFile(ResourceLocation location, ResourceManager manager) {
		return GsonHelper.fromJson(GSON, getFileContents(location, manager), JsonObject.class);
	}

	public static String getFileContents(ResourceLocation location, ResourceManager manager) {
		try (InputStream inputStream = manager.getResourceOrThrow(location).open()) {
			return IOUtils.toString(inputStream, Charset.defaultCharset());
		}
		catch (Exception e) {
			PandaLib.LOGGER.error("Couldn't load " + location, e);

			throw new RuntimeException(new FileNotFoundException(location.toString()));
		}
	}
}

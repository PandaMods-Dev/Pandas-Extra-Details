package me.pandamods.pandalib.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.utils.gsonadapter.QuaternionfTypeAdapter;
import me.pandamods.pandalib.utils.gsonadapter.Vector3fTypeAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.io.IOUtils;
import org.joml.Quaternionf;
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
	private static final String SUPPORTED_MESH_VERSION = "0.3";
	private static final String SUPPORTED_ARMATURE_VERSION = "0.1";
	private static final String SUPPORTED_ANIMATION_VERSION = "0.2";

	public static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(Vector3f.class, new Vector3fTypeAdapter())
			.registerTypeAdapter(Quaternionf.class, new QuaternionfTypeAdapter())
			.create();

	public static Map<ResourceLocation, MeshRecord> MESHES = new HashMap<>();
	public static Map<ResourceLocation, ArmatureRecord> ARMATURES = new HashMap<>();
	public static Map<ResourceLocation, AnimationRecord> ANIMATIONS = new HashMap<>();

	public static void registerReloadListener() {
		Minecraft mc = Minecraft.getInstance();
		if (!(mc.getResourceManager() instanceof ReloadableResourceManager resourceManager))
			throw new RuntimeException("PandaLib was initialized too early!");

		resourceManager.registerReloadListener(Resources::reload);
	}

	public static CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparationBarrier, ResourceManager manager,
												 ProfilerFiller profilerFiller, ProfilerFiller profilerFiller1,
												 Executor executor, Executor executor1) {
		Map<ResourceLocation, MeshRecord> meshes = new Object2ObjectOpenHashMap<>();
		Map<ResourceLocation, ArmatureRecord> armatures = new Object2ObjectOpenHashMap<>();
		Map<ResourceLocation, AnimationRecord> animations = new Object2ObjectOpenHashMap<>();

		return CompletableFuture.allOf(
						load("mesh", "meshes", SUPPORTED_MESH_VERSION, MeshRecord.class,
								executor, manager, meshes::put),
						load("armature", "armatures", SUPPORTED_ARMATURE_VERSION, ArmatureRecord.class,
								executor, manager, armatures::put),
						load("animation", "animations", SUPPORTED_ANIMATION_VERSION, AnimationRecord.class,
								executor, manager, animations::put)
				).thenCompose(preparationBarrier::wait)
				.thenAcceptAsync(unused -> Resources.MESHES = meshes, executor1)
				.thenAcceptAsync(unused -> Resources.ARMATURES = armatures, executor1)
				.thenAcceptAsync(unused -> Resources.ANIMATIONS = animations, executor1);
	}

	private static <C> CompletableFuture<Void> load(String typeName, String directory, String formatVersion, Class<C> modelClass,
												Executor executor, ResourceManager resourceManager,
												BiConsumer<ResourceLocation, C> map) {
		return CompletableFuture.supplyAsync(() ->
				resourceManager.listResources("pandalib/" + directory, resource ->
						resource.getPath().endsWith(".json")), executor).thenApplyAsync(resources -> {
			Map<ResourceLocation, CompletableFuture<C>> tasks = new Object2ObjectOpenHashMap<>();

			for (ResourceLocation resource : resources.keySet()) {
				JsonObject json = loadFile(resource, resourceManager);
				if (!json.has("format_version") || !json.get("format_version").getAsString().equals(formatVersion)) {
					PandaLib.LOGGER.error(typeName + " format version " + formatVersion + " is not supported");
					continue;
				}

				C file = GSON.fromJson(json, modelClass);
				tasks.put(resource, CompletableFuture.supplyAsync(() -> file, executor));
			}

			return tasks;
		}, executor).thenAcceptAsync(resource -> {
			for (Map.Entry<ResourceLocation, CompletableFuture<C>> entry : resource.entrySet()) {
				map.accept(entry.getKey(), entry.getValue().join());
			}
		}, executor);
	}

	public static AnimationRecord loadFiles(ResourceLocation location, ResourceManager manager) {
		return GSON.fromJson(loadFile(location, manager), AnimationRecord.class);
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

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
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.*;
import org.lwjgl.assimp.*;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Resources implements PreparableReloadListener {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(Vector2fc.class, new Vector2fTypeAdapter())
			.registerTypeAdapter(Vector3fc.class, new Vector3fTypeAdapter())
			.registerTypeAdapter(Quaternionfc.class, new QuaternionfTypeAdapter())
			.registerTypeAdapter(Matrix4fc.class, new Matrix4fTypeAdapter())
			.create();

	private static final List<ResourceLocation> missingResources = new ObjectArrayList<>();

	public Map<ResourceLocation, Mesh> meshes = new Object2ObjectOpenHashMap<>();
	public Map<ResourceLocation, Animation> animations = new Object2ObjectOpenHashMap<>();

	public static Mesh getMesh(ResourceLocation resourceLocation) {
		return getMesh(resourceLocation, false);
	}

	public static Mesh getMesh(ResourceLocation resourceLocation, boolean allowNull) {
		try {
			if (!allowNull && !ExtraDetails.resources.meshes.containsKey(resourceLocation))
				throw new ResourceLocationException(resourceLocation.toString());
			return ExtraDetails.resources.meshes.get(resourceLocation);
		} catch (ResourceLocationException e) {
			throw createCrashReport(e, resourceLocation);
		}
	}

	public static Animation getAnimation(ResourceLocation resourceLocation) {
		return getAnimation(resourceLocation, false);
	}

	public static Animation getAnimation(ResourceLocation resourceLocation, boolean allowNull) {
		try {
			if (!allowNull && !ExtraDetails.resources.animations.containsKey(resourceLocation))
				throw new ResourceLocationException(resourceLocation.toString());
			return ExtraDetails.resources.animations.get(resourceLocation);
		} catch (ResourceLocationException e) {
			throw createCrashReport(e, resourceLocation);
		}
	}

	private static RuntimeException createCrashReport(Throwable cause, ResourceLocation resourceLocation) {
		CrashReport crashReport = CrashReport.forThrowable(cause, "Couldn't find " + resourceLocation.toString());
    	return new ReportedException(crashReport);
	}

	@Override
	public @NotNull CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager,
												   ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler,
												   Executor backgroundExecutor, Executor gameExecutor) {
		List<AIScene> scenes = new ObjectArrayList<>();

		Map<ResourceLocation, Mesh> meshes = new Object2ObjectOpenHashMap<>();
		Map<ResourceLocation, Animation> animations = new Object2ObjectOpenHashMap<>();

		return CompletableFuture.allOf(loadAssimpScene(backgroundExecutor, resourceManager, scenes::add, meshes::put, animations::put))
				.thenCompose(preparationBarrier::wait)
				.thenAcceptAsync(unused -> {
					this.meshes = meshes;
					this.animations = animations;
					scenes.forEach(Assimp::aiReleaseImport);
				}, gameExecutor);
	}

	private CompletableFuture<Void> loadAssimpScene(Executor executor, ResourceManager resourceManager,
													Consumer<AIScene> addScene,
													BiConsumer<ResourceLocation, Mesh> putMesh,
													BiConsumer<ResourceLocation, Animation> putAnimation
	) {
		return CompletableFuture.supplyAsync(() -> resourceManager.listResources("assimp", resource -> true), executor)
				.thenApplyAsync(resources -> {
					Map<ResourceLocation, CompletableFuture<AIScene>> sceneTasks = new HashMap<>();

					for (ResourceLocation resourceLocation : resources.keySet()) {
						AIScene scene = loadAssimpScene(resourceManager, resourceLocation);
						addScene.accept(scene);

						if (scene != null) sceneTasks.put(resourceLocation, CompletableFuture.supplyAsync(() -> scene, executor));
					}
					return sceneTasks;
				}, executor)
				.thenAcceptAsync(resource -> {
					for (Map.Entry<ResourceLocation, CompletableFuture<AIScene>> entry : resource.entrySet()) {
						ResourceLocation resourceLocation = entry.getKey();
						AIScene scene = entry.getValue().join();

						List<AIMesh> meshes = new ObjectArrayList<>();
						List<AIMaterial> materials = new ObjectArrayList<>();

						for (int i = 0; i < scene.mNumMeshes(); i++)
							meshes.add(AIMesh.create(scene.mMeshes().get(i)));

						for (int i = 0; i < scene.mNumMaterials(); i++)
							materials.add(AIMaterial.create(scene.mMaterials().get(i)));

						putMesh.accept(resourceLocation, new Mesh(meshes, materials));

						for (int i = 0; i < scene.mNumAnimations(); i++) {
							AIAnimation animation = AIAnimation.create(scene.mAnimations().get(i));
							ResourceLocation animationLocation = resourceLocation;
							if (scene.mNumAnimations() > 1) animationLocation.withSuffix("/" + animation.mName().dataString());

							putAnimation.accept(resourceLocation, new Animation(animation));
						}
					}
				}, executor);
	}

	private AIScene loadAssimpScene(ResourceManager resourceManager, ResourceLocation resourceLocation) {
		try (InputStream inputStream = resourceManager.getResourceOrThrow(resourceLocation).open()) {
			byte[] bytes = inputStream.readAllBytes();
			ByteBuffer buffer = ByteBuffer.allocateDirect(bytes.length);
			buffer.put(bytes);
			buffer.flip();
			return Assimp.aiImportFileFromMemory(buffer,
					Assimp.aiProcess_Triangulate | Assimp.aiProcess_JoinIdenticalVertices |
							Assimp.aiProcess_PopulateArmatureData, "");
		}
		catch (Exception e) {
			throw new RuntimeException(new FileNotFoundException(resourceLocation.toString()));
		}
	}
}

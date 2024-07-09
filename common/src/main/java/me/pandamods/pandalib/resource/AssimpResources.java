package me.pandamods.pandalib.resource;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.assimp.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AssimpResources implements PreparableReloadListener {
	private static Map<ResourceLocation, Mesh> MESHES = new Object2ObjectOpenHashMap<>();
	private static Map<ResourceLocation, Animation> ANIMATIONS = new Object2ObjectOpenHashMap<>();

	/**
	 * Retrieves a Mesh object associated with the given resource location.
	 *
	 * @param resourceLocation The resource location of the mesh.
	 * @return The Mesh object associated with the resource location.
	 * Mesh object might be empty if the Mesh was never loaded.
	 */
	public static Mesh getMesh(ResourceLocation resourceLocation) {
		Mesh mesh = MESHES.get(resourceLocation);
		if (mesh == null) MESHES.put(resourceLocation, mesh = new Mesh());
		return mesh;
	}

	/**
	 * Retrieves an Animation object associated with the given resource location.
	 *
	 * @param resourceLocation The resource location of the animation.
	 * @return The Animation object associated with the resource location.
	 * Animation object might be empty if the Animation was never loaded.
	 */
	public static Animation getAnimation(ResourceLocation resourceLocation) {
		Animation animation = ANIMATIONS.get(resourceLocation);
		if (animation == null) ANIMATIONS.put(resourceLocation, animation = new Animation());
		return animation;
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
					MESHES = meshes;
					ANIMATIONS = animations;
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

						putMesh.accept(resourceLocation, AssimpResources.getMesh(resourceLocation).set(meshes, materials));

						for (int i = 0; i < scene.mNumAnimations(); i++) {
							AIAnimation animation = AIAnimation.create(scene.mAnimations().get(i));
							ResourceLocation animationLocation = resourceLocation;
							if (scene.mNumAnimations() > 1) animationLocation.withSuffix("/" + animation.mName().dataString());

							putAnimation.accept(animationLocation, AssimpResources.getAnimation(animationLocation).set(animation));
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
					Assimp.aiProcess_Triangulate | Assimp.aiProcess_PopulateArmatureData, "");
		}
		catch (Exception e) {
			throw new RuntimeException(new FileNotFoundException(resourceLocation.toString()));
		}
	}
}

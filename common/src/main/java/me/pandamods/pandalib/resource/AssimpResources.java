package me.pandamods.pandalib.resource;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.pandamods.extra_details.ExtraDetails;
import net.minecraft.CrashReport;
import net.minecraft.ReportedException;
import net.minecraft.ResourceLocationException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.assimp.*;
import org.slf4j.Logger;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class AssimpResources implements PreparableReloadListener {
	private static final Logger LOGGER = LogUtils.getLogger();
	private static final List<ResourceLocation> missingResources = new ObjectArrayList<>();

	public final Map<ResourceLocation, Mesh> meshes = new Object2ObjectOpenHashMap<>();
	public final Map<ResourceLocation, Animation> animations = new Object2ObjectOpenHashMap<>();

	/**
	 * Retrieves a Mesh object associated with the given resource location.
	 *
	 * @param resourceLocation The resource location of the mesh.
	 * @return The Mesh object associated with the resource location.
	 * Mesh object might be empty if the Mesh was never loaded.
	 */
	public static Mesh getMesh(ResourceLocation resourceLocation) {
		AssimpResources resources = ExtraDetails.ASSIMP_RESOURCES;
		Mesh mesh = resources.meshes.get(resourceLocation);
		if (mesh == null) resources.meshes.put(resourceLocation, mesh = new Mesh());
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
		AssimpResources resources = ExtraDetails.ASSIMP_RESOURCES;
		Animation animation = resources.animations.get(resourceLocation);
		if (animation == null) resources.animations.put(resourceLocation, animation = new Animation());
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
					this.meshes.clear();
					this.animations.clear();
					this.meshes.putAll(meshes);
					this.animations.putAll(animations);
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
					Assimp.aiProcess_Triangulate | Assimp.aiProcess_JoinIdenticalVertices |
							Assimp.aiProcess_PopulateArmatureData, "");
		}
		catch (Exception e) {
			throw new RuntimeException(new FileNotFoundException(resourceLocation.toString()));
		}
	}
}

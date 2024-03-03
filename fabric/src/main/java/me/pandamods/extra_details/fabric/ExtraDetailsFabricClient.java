package me.pandamods.extra_details.fabric;

import me.pandamods.extra_details.ExtraDetails;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ExtraDetailsFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
			@Override
			public ResourceLocation getFabricId() {
				return new ResourceLocation(ExtraDetails.MOD_ID, "general");
			}

			@Override
			public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager,
												  ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler,
												  Executor backgroundExecutor, Executor gameExecutor) {
				return CompletableFuture.allOf(
						ExtraDetails.blockRenderDispatcher.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler,
								backgroundExecutor, gameExecutor),
						ExtraDetails.RESOURCES.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler,
								backgroundExecutor, gameExecutor)
				);
			}
		});

		ExtraDetails.client();
	}
}
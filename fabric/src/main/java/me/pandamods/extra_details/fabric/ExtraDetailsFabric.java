package me.pandamods.extra_details.fabric;

import me.pandamods.extra_details.ExtraDetails;
import net.fabricmc.api.ModInitializer;

public class ExtraDetailsFabric implements ModInitializer {
	@Override
	public void onInitialize() {
//		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
//			@Override
//			public ResourceLocation getFabricId() {
//				return new ResourceLocation(ExtraDetails.MOD_ID, "meshes");
//			}
//
//			@Override
//			public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager,
//												  ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler,
//												  Executor backgroundExecutor, Executor gameExecutor) {
//				return Resources.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler,
//						backgroundExecutor, gameExecutor);
//			}
//		});

		ExtraDetails.init();
	}
}
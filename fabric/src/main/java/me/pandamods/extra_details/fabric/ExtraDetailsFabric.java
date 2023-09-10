package me.pandamods.extra_details.fabric;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.resources.Resources;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ExtraDetailsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new IdentifiableResourceReloadListener() {
			@Override
			public ResourceLocation getFabricId() {
				return new ResourceLocation(ExtraDetails.MOD_ID, "meshes");
			}

			@Override
			public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler,
												  ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
				return Resources.reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
			}
		});

        ExtraDetails.init();
    }
}
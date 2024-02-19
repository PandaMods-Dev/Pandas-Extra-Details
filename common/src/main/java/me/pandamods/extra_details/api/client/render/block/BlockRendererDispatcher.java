package me.pandamods.extra_details.api.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class BlockRendererDispatcher implements ResourceManagerReloadListener {
	private Map<Block, BlockRenderer> renderers = new HashMap<>();

	public BlockRenderer getRenderer(Block block) {
		return this.renderers.get(block);
	}

	public boolean isRendererRegistered(Block block) {
		return renderers.containsKey(block);
	}

	public void render(BlockPos blockPos, Level level, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick) {
		BlockRenderer renderer = getRenderer(level.getBlockState(blockPos).getBlock());
		if (renderer == null) return;
		renderer.render(poseStack, bufferSource, partialTick, getLightColor(level, blockPos));
	}

	public static int getLightColor(Level level, BlockPos blockPos) {
		if (level != null) {
			return LevelRenderer.getLightColor(level, blockPos);
		} else {
			return  15728880;
		}
	}

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		BlockRendererRegistry rendererRegistry = new BlockRendererRegistry();
		ExtraDetails.rendererRegistryEvent.invoker().register(rendererRegistry);
		renderers = rendererRegistry.renderers;
	}
}

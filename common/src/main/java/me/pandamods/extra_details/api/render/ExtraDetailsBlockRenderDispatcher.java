package me.pandamods.extra_details.api.render;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Map;

public class ExtraDetailsBlockRenderDispatcher {
	private Map<Block, BlockRenderer> renderers = ImmutableMap.of(
			Blocks.LEVER, (blockPos, level1, partialTick, poseStack, bufferSource, packedLight, packedOverlay) -> {
				VertexConsumer buffer = bufferSource.getBuffer(RenderType.LINES);
				buffer.addVertex(poseStack.last(), 0, 0, 0).setColor(Color.white.getRGB()).setNormal(poseStack.last(), 0, 0, 1);
				buffer.addVertex(poseStack.last(), 0, 1, 0).setColor(Color.white.getRGB()).setNormal(poseStack.last(), 0, 0, 1);
			}
	);
	private ClientLevel level;
	private Camera camera;
	private HitResult cameraHitResult;

	@Nullable
	public BlockRenderer getRenderer(Block block) {
		return this.renderers.get(block);
	}
	
	public void prepare(ClientLevel level, Camera camera, HitResult cameraHitResult) {
		if (this.level != level) {
			this.setLevel(level);
		}

		this.camera = camera;
		this.cameraHitResult = cameraHitResult;
	}

	public void render(BlockPos blockPos, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
		BlockState blockState = level.getBlockState(blockPos);
		Block block = blockState.getBlock();
		BlockRenderer renderer = this.getRenderer(block);
		if (renderer != null && renderer.shouldRender(blockPos, level, this.camera.getPosition())) {
			setupAndRender(renderer, blockPos, level, partialTick, poseStack, bufferSource);
		}
	}
	
	private static void setupAndRender(BlockRenderer renderer, BlockPos blockPos, ClientLevel level, float partialTick, 
															   PoseStack poseStack, MultiBufferSource bufferSource) {
		int light;
		if (level != null) {
			light = LevelRenderer.getLightColor(level, blockPos);
		} else {
			light = 15728880;
		}

		renderer.render(blockPos, level, partialTick, poseStack, bufferSource, light, OverlayTexture.NO_OVERLAY);
	}

	public void setLevel(@Nullable ClientLevel level) {
		this.level = level;
		if (level == null) {
			this.camera = null;
		}
	}
}

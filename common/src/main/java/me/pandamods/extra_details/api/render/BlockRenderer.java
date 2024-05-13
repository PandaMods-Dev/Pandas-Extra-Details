package me.pandamods.extra_details.api.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;

public interface BlockRenderer {
	void render(BlockPos blockPos, ClientLevel level, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightmapUV);

	default RenderShape getRenderShape() {
		return RenderShape.INVISIBLE;
	}
}

package me.pandamods.pandalib.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public interface ClientBlockRenderer<T extends ClientBlock> {
	void render(T block, PoseStack poseStack, MultiBufferSource buffer, int lightColor, int overlay, float partialTick);

	default RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}
}

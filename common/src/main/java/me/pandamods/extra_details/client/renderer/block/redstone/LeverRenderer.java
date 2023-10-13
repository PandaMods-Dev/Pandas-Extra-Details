package me.pandamods.extra_details.client.renderer.block.redstone;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.model.block.LeverModel;
import me.pandamods.extra_details.entity.block.LeverClientBlock;
import me.pandamods.pandalib.client.render.block.extensions.MeshClientBlockRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class LeverRenderer extends MeshClientBlockRenderer<LeverClientBlock, LeverModel> {
	public LeverRenderer() {
		super(new LeverModel());
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return ExtraDetails.getConfig().enable_lever_animation ? RenderShape.INVISIBLE : RenderShape.MODEL;
	}

	@Override
	public void render(LeverClientBlock block, PoseStack poseStack, MultiBufferSource buffer, int lightColor, int overlay, float partialTick) {
		if (ExtraDetails.getConfig().enable_lever_animation) {
			super.render(block, poseStack, buffer, lightColor, overlay, partialTick);
		}
	}
}
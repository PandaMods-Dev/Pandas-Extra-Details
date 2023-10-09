package me.pandamods.extra_details.client.renderer.block.door;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.model.block.FenceGateModel;
import me.pandamods.extra_details.entity.block.FenceGateClientBlock;
import me.pandamods.pandalib.client.render.MeshBlockEntityRenderer;
import me.pandamods.pandalib.client.render.block.extensions.MeshClientBlockRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

public class FenceGateRenderer extends MeshClientBlockRenderer<FenceGateClientBlock, FenceGateModel> {
	public FenceGateRenderer() {
		super(new FenceGateModel());
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return ExtraDetails.getConfig().enable_fence_gate_animation ? RenderShape.INVISIBLE : RenderShape.MODEL;
	}

	@Override
	public void render(FenceGateClientBlock block, PoseStack poseStack, MultiBufferSource buffer, int lightColor, int overlay, float partialTick) {
		if (ExtraDetails.getConfig().enable_fence_gate_animation) {
			poseStack.pushPose();
			if (block.getBlockState().getValue(FenceGateBlock.IN_WALL)) {
				poseStack.translate(0, (float) -3 /16, 0);
			}
			super.render(block, poseStack, buffer, lightColor, overlay, partialTick);
			poseStack.popPose();
		}
	}
}

package me.pandamods.extra_details.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.extra_details.api.client.render.block.ClientBlockEntityRenderer;
import me.pandamods.extra_details.client.clientblockentity.LeverBlockEntity;
import me.pandamods.extra_details.client.model.LeverModel;
import me.pandamods.pandalib.client.Model;
import me.pandamods.pandalib.client.mesh.MeshRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class LeverRenderer implements ClientBlockEntityRenderer<LeverBlockEntity>, MeshRenderer<LeverBlockEntity, LeverModel> {
	public LeverModel model = new LeverModel();

	@Override
	public void render(LeverBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor) {
		poseStack.pushPose();
		poseStack.translate(0, 1, 0);
		translateBlock(blockEntity.getBlockState(), poseStack);
		renderGeometry(blockEntity, null, poseStack, bufferSource, lightColor, OverlayTexture.NO_OVERLAY);
		poseStack.popPose();
	}

	@Override
	public LeverModel getModel() {
		return model;
	}

	public static void translateBlock(BlockState blockState, PoseStack poseStack) {
		poseStack.translate(0.5f, 0.5f, 0.5f);

		float direction = getYRotation(blockState);
		poseStack.mulPose(Axis.YP.rotationDegrees(direction));

		if (blockState.hasProperty(BlockStateProperties.ATTACH_FACE)) {
			AttachFace face = blockState.getValue(BlockStateProperties.ATTACH_FACE);
			switch (face) {
				case CEILING -> poseStack.mulPose(Axis.XP.rotationDegrees(180));
				case WALL -> poseStack.mulPose(Axis.XP.rotationDegrees(90));
			}
		}

		poseStack.translate(0, -0.5f, 0);
	}

	public static float getYRotation(BlockState blockState) {
		if (blockState.hasProperty(BlockStateProperties.ROTATION_16))
			return (360f/16f) * blockState.getValue(BlockStateProperties.ROTATION_16);

		if (blockState.hasProperty(HorizontalDirectionalBlock.FACING))
			return -blockState.getValue(HorizontalDirectionalBlock.FACING).toYRot();

		if (blockState.hasProperty(DirectionalBlock.FACING))
			return -blockState.getValue(DirectionalBlock.FACING).toYRot();

		return 0;
	}
}

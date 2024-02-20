package me.pandamods.pandalib.client.mesh;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.extra_details.api.client.render.block.BlockContext;
import me.pandamods.extra_details.api.client.render.block.BlockRenderer;
import me.pandamods.pandalib.client.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class MeshBlockRenderer<M extends Model<BlockContext>> implements BlockRenderer, MeshRenderer<BlockContext> {
	public M model;

	public MeshBlockRenderer(M model) {
		this.model = model;
	}

	@Override
	public M getModel() {
		return model;
	}

	@Override
	public void render(BlockContext blockContext, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor) {
		poseStack.pushPose();
		translateBlock(blockContext.blockState(), poseStack);
		renderGeometry(blockContext, poseStack, bufferSource, lightColor, OverlayTexture.NO_OVERLAY);
		poseStack.popPose();
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

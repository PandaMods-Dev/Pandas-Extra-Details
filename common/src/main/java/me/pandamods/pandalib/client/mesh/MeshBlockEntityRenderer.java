package me.pandamods.pandalib.client.mesh;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.clientblockentity.renderer.ClientBlockEntityRenderer;
import me.pandamods.pandalib.client.Model;
import me.pandamods.pandalib.client.animation.AnimationController;
import me.pandamods.pandalib.client.armature.ArmatureAnimator;
import me.pandamods.pandalib.client.armature.IAnimatable;
import me.pandamods.pandalib.utils.RenderUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class MeshBlockEntityRenderer<T extends BlockEntity & IAnimatable, M extends Model<T>, AC extends AnimationController<T>>
		implements BlockEntityRenderer<T>, MeshRenderer<T, M>, ArmatureAnimator<T, AC> {
	private final M model;
	private final AC animationController;

	public MeshBlockEntityRenderer(M model, AC animationController) {
		this.model = model;
		this.animationController = animationController;
	}

	@Override
	public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		poseStack.pushPose();
		translateBlock(blockEntity.getBlockState(), poseStack);
		animateArmature(blockEntity, partialTick);
		renderGeometry(blockEntity, blockEntity.animatableCache().armature, poseStack, buffer, packedLight, packedOverlay);
		poseStack.popPose();
	}

	@Override
	public M getModel() {
		return this.model;
	}

	@Override
	public AC getController() {
		return this.animationController;
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

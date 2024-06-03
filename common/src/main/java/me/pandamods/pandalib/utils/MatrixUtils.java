package me.pandamods.pandalib.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MatrixUtils {
	public static Matrix4f lerp(Matrix4f main, Matrix4f other, float alpha) {
		Vector3f mainTranslation = new Vector3f();
		Quaternionf mainRotation = new Quaternionf();
		Vector3f mainScale = new Vector3f();
		Vector3f otherTranslation = new Vector3f();
		Quaternionf otherRotation = new Quaternionf();
		Vector3f otherScale = new Vector3f();

		main.getTranslation(mainTranslation);
		main.getUnnormalizedRotation(mainRotation);
		main.getScale(mainScale);
		other.getTranslation(otherTranslation);
		other.getUnnormalizedRotation(otherRotation);
		other.getScale(otherScale);

		Vector3f lerpedTranslation = new Vector3f();
		Quaternionf lerpedRotation = new Quaternionf();
		Vector3f lerpedScale = new Vector3f();

		mainTranslation.lerp(otherTranslation, alpha, lerpedTranslation);
		mainRotation.slerp(otherRotation, alpha, lerpedRotation);
		mainScale.lerp(otherScale, alpha, lerpedScale);

		return main.identity().translationRotateScale(lerpedTranslation, lerpedRotation, lerpedScale);
	}

	public static void translateBlock(BlockState blockState, PoseStack poseStack) {
		poseStack.translate(0.5f, 0.5f, 0.5f);
		float direction = getBlockYRotation(blockState);
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

	public static float getBlockYRotation(BlockState blockState) {
		if (blockState.hasProperty(BlockStateProperties.ROTATION_16))
			return (360f/16f) * blockState.getValue(BlockStateProperties.ROTATION_16);

		if (blockState.hasProperty(HorizontalDirectionalBlock.FACING))
			return -blockState.getValue(HorizontalDirectionalBlock.FACING).toYRot();

		if (blockState.hasProperty(DirectionalBlock.FACING))
			return -blockState.getValue(DirectionalBlock.FACING).toYRot();

		return 0;
	}
}

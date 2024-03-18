package me.pandamods.pandalib.client.armature;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.client.animation.AnimationController;
import me.pandamods.pandalib.client.animation.AnimationHandler;
import me.pandamods.pandalib.resource.ArmatureData;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.*;
import org.joml.Math;

import java.util.Objects;

public interface ArmatureAnimator<animatable extends IAnimatable, AnimController extends AnimationController<animatable>> {
	AnimController getController();

	@SuppressWarnings("unchecked")
	default void animateArmature(animatable animatable, float partialTick) {
		AnimController controller = getController();
		if (controller.armatureLocation(animatable) == null) return;

		Armature armature;
		if (!Objects.equals(animatable.animatableCache().resourceLocation, controller.armatureLocation(animatable))) {
			ArmatureData armatureData = ExtraDetails.resources.armatures.get(controller.armatureLocation(animatable));
			animatable.animatableCache().armature = armature = new Armature(armatureData);
			animatable.animatableCache().resourceLocation = controller.armatureLocation(animatable);
		} else armature = animatable.animatableCache().armature;

		AnimationHandler<animatable> animationHandler;
		if (animatable.animatableCache().animationHandler == null) {
			animationHandler = new AnimationHandler<>(animatable, controller);
			animatable.animatableCache().animationHandler = (AnimationHandler<? extends AnimatableCache>) animationHandler;
		} else animationHandler = (AnimationHandler<animatable>) animatable.animatableCache().animationHandler;

		controller.preMathAnimate(animatable, armature, partialTick);
		animationHandler.update(armature, animatable.getTick(partialTick) / 20);
		controller.postMathAnimate(animatable, armature, partialTick);

		armature.getBones().values().forEach(bone -> {
			if (bone.parent == null) bone.updateTransform();
		});
	}

	default void renderArmatureDebug(animatable animatable, PoseStack poseStack, MultiBufferSource bufferSource) {
		Armature armature = animatable.animatableCache().armature;
		if (armature != null) {
			armature.getBones().values().forEach(bone -> {
				poseStack.pushPose();
				poseStack.mulPoseMatrix(new Matrix4f(bone.getGlobalTransform()).rotateX(Math.toRadians(-90)));
				LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()),
						AABB.ofSize(new Vec3(0, .2f, 0), 0f, .3f, 0f),
						1, 1, 1, 1);
				LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()),
						AABB.ofSize(new Vec3(0, 0, 0), 0.1f, 0.1f, 0.1f),
						1, 1, 1, 1);
				poseStack.popPose();
			});
		}
	}
}

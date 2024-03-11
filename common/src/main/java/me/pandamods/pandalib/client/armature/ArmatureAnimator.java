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

public interface ArmatureAnimator<Cache extends IAnimatable, AnimController extends AnimationController<Cache>> {
	AnimController getController();

	@SuppressWarnings("unchecked")
	default void animateArmature(Cache cache, float partialTick, float time) {
		AnimController controller = getController();
		if (controller.armatureLocation(cache) == null) return;

		Armature armature;
		if (!Objects.equals(cache.animatableCache().resourceLocation, controller.armatureLocation(cache))) {
			ArmatureData armatureData = ExtraDetails.RESOURCES.armatures.get(controller.armatureLocation(cache));
			cache.animatableCache().armature = armature = new Armature(armatureData);
			cache.animatableCache().resourceLocation = controller.armatureLocation(cache);
		} else armature = cache.animatableCache().armature;

		AnimationHandler<Cache> animationHandler;
		if (cache.animatableCache().animationHandler == null) {
			animationHandler = new AnimationHandler<>(cache, controller);
			cache.animatableCache().animationHandler = (AnimationHandler<? extends AnimatableCache>) animationHandler;
		} else animationHandler = (AnimationHandler<Cache>) cache.animatableCache().animationHandler;

		animationHandler.update(armature, time);
		controller.mathAnimate(cache, armature, partialTick);
		armature.getBones().values().forEach(bone -> {
			if (bone.parent == null) bone.updateTransform();
		});
	}

	default void renderArmatureDebug(Cache cache, PoseStack poseStack, MultiBufferSource bufferSource) {
		Armature armature = cache.animatableCache().armature;
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

package me.pandamods.pandalib.client.armature;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.client.animation.AnimationController;
import me.pandamods.pandalib.resource.ArmatureData;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.*;

import java.util.Objects;

public interface ArmatureAnimator<Cache extends IArmatureCache, AnimController extends AnimationController<Cache>> {
	AnimController getController();

	default void animateArmature(Cache cache, float partialTick) {
		AnimController controller = getController();
		if (controller.armatureLocation(cache) == null) return;
		Armature armature;
		if (!Objects.equals(cache.armatureCache().resourceLocation, controller.armatureLocation(cache))) {
			ArmatureData armatureData = ExtraDetails.RESOURCES.armatures.get(controller.armatureLocation(cache));
			armature = new Armature(armatureData);
			cache.armatureCache().armature = armature;
			cache.armatureCache().resourceLocation = controller.armatureLocation(cache);
		} else armature = cache.armatureCache().armature;

		controller.animate(cache, armature, partialTick);
		armature.getBones().values().forEach(bone -> {
			if (bone.parent == null) bone.updateTransform();
		});
	}

	default void renderArmatureDebug(Cache cache, PoseStack poseStack, MultiBufferSource bufferSource) {
		Armature armature = cache.armatureCache().armature;
		if (armature != null) {
			armature.getBones().values().forEach(bone -> {
				poseStack.pushPose();
				poseStack.mulPoseMatrix(new Matrix4f(bone.getGlobalTransform()));
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

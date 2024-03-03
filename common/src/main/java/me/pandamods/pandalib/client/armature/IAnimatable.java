package me.pandamods.pandalib.client.armature;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.resource.ArmatureData;

import java.util.Objects;

public interface IAnimatable<Cache extends IAnimatableCache, AnimController extends AnimationController<Cache>> {
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
		armature.getBones().values().forEach(Bone::updateGlobalTransform);
	}
}

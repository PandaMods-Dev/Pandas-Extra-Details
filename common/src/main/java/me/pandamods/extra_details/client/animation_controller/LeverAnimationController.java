package me.pandamods.extra_details.client.animation_controller;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.client.armature.AnimationController;
import me.pandamods.pandalib.client.armature.Armature;
import me.pandamods.pandalib.client.armature.IAnimatableCache;
import net.minecraft.resources.ResourceLocation;

public class LeverAnimationController implements AnimationController<IAnimatableCache> {
	@Override
	public ResourceLocation armatureLocation(IAnimatableCache iAnimatableCache) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/armatures/block/redstone/lever.json");
	}

	@Override
	public void animate(IAnimatableCache iAnimatableCache, Armature armature, float partialTick) {
		armature.getBone("handle").ifPresent(bone -> bone.localTransform.rotateX(45));
	}
}

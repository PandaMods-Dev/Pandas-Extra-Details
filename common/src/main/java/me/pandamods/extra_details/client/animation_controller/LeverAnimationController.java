package me.pandamods.extra_details.client.animation_controller;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.clientblockentity.LeverBlockEntity;
import me.pandamods.pandalib.client.animation.AnimationController;
import me.pandamods.pandalib.client.armature.Armature;
import net.minecraft.resources.ResourceLocation;
import org.joml.Math;

public class LeverAnimationController implements AnimationController<LeverBlockEntity> {
	@Override
	public ResourceLocation armatureLocation(LeverBlockEntity leverBlockEntity) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/armatures/block/redstone/lever.json");
//		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/armatures/debug2.json");
	}

	@Override
	public void animate(LeverBlockEntity leverBlockEntity, Armature armature, float partialTick) {
		armature.getBone("handle").ifPresent(bone -> bone.localTransform.setRotationXYZ(Math.toRadians(-45), 0, 0));

//		armature.getBone("Bone1").ifPresent(bone -> bone.localTransform.setRotationXYZ(Math.toRadians(-90), 0, 0));
//		armature.getBone("Bone2").ifPresent(bone -> bone.localTransform.setRotationXYZ(Math.toRadians(-90), 0, 0));
//		armature.getBone("Bone3").ifPresent(bone -> bone.localTransform.setRotationXYZ(Math.toRadians(-90), 0, 0));

//		armature.getBone("Bone1").ifPresent(bone -> bone.localTransform.setTranslation(0, 0, 1));

//		armature.getBone("Bone1").ifPresent(bone -> bone.localTransform.setRotationXYZ(Math.toRadians(-45), 0, 0));
//		armature.getBone("Bone2").ifPresent(bone -> bone.localTransform.setRotationXYZ(Math.toRadians(-45), 0, 0));
//		armature.getBone("Bone3").ifPresent(bone -> bone.localTransform.setRotationXYZ(Math.toRadians(-45), 0, 0));
	}
}

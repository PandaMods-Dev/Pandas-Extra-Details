package me.pandamods.extra_details.client.animationcontroller;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.clientblockentity.DoorBlockEntity;
import me.pandamods.extra_details.client.clientblockentity.LeverBlockEntity;
import me.pandamods.pandalib.client.animation.AnimationController;
import me.pandamods.pandalib.client.animation.AnimationState;
import me.pandamods.pandalib.client.armature.Armature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import org.joml.Vector3f;

public class DoorAnimationController implements AnimationController<DoorBlockEntity> {
	@Override
	public ResourceLocation armatureLocation(DoorBlockEntity doorBlockEntity) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/armatures/block/door/door.json");
	}

	@Override
	public AnimationState<DoorBlockEntity> registerAnimations() {
		AnimationState<DoorBlockEntity> close = animate(ExtraDetails.MOD_ID, "pandalib/animations/block/door/door_close.json");
		AnimationState<DoorBlockEntity> open = animate(ExtraDetails.MOD_ID, "pandalib/animations/block/door/door_open.json");

		close.registerBranch(open, 0.1f, (doorBlockEntity, state) -> doorBlockEntity.getBlockState().getValue(DoorBlock.OPEN));
		open.registerBranch(close, 0.1f, (doorBlockEntity, state) -> !doorBlockEntity.getBlockState().getValue(DoorBlock.OPEN));
		return close;
	}

	@Override
	public void preMathAnimate(DoorBlockEntity doorBlockEntity, Armature armature, float partialTick) {
		if (doorBlockEntity.getBlockState().getValue(DoorBlock.HINGE).equals(DoorHingeSide.RIGHT)) {
			armature.getBone("door").ifPresent(bone -> {
				bone.resetInitialTransform();
				Vector3f translation = bone.initialTransform.getTranslation(new Vector3f());
				bone.initialTransform.setTranslation(-translation.x, translation.y, translation.z);
			});
		}
	}

	@Override
	public void postMathAnimate(DoorBlockEntity doorBlockEntity, Armature armature, float partialTick) {
		if (doorBlockEntity.getBlockState().getValue(DoorBlock.HINGE).equals(DoorHingeSide.RIGHT)) {
			armature.getBone("door").ifPresent(bone -> {
				Vector3f rotation = bone.localTransform.getEulerAnglesXYZ(new Vector3f());
				bone.localTransform.setRotationZYX(rotation.z, -rotation.y, rotation.x);
			});
		}
	}
}

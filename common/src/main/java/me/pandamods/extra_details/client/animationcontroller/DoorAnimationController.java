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
	public void mathAnimate(DoorBlockEntity doorBlockEntity, Armature armature, float partialTick) {
	}
}

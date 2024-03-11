package me.pandamods.extra_details.client.animationcontroller;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.clientblockentity.LeverBlockEntity;
import me.pandamods.pandalib.client.animation.AnimationController;
import me.pandamods.pandalib.client.animation.AnimationState;
import me.pandamods.pandalib.client.armature.Armature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LeverBlock;

public class LeverAnimationController implements AnimationController<LeverBlockEntity> {
	@Override
	public ResourceLocation armatureLocation(LeverBlockEntity leverBlockEntity) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/armatures/block/redstone/lever.json");
	}

	@Override
	public AnimationState<LeverBlockEntity> registerAnimations() {
		AnimationState<LeverBlockEntity> root = animate(ExtraDetails.MOD_ID, "pandalib/animations/block/redstone/lever_off.json");
		AnimationState<LeverBlockEntity> on = animate(ExtraDetails.MOD_ID, "pandalib/animations/block/redstone/lever_on.json");

		root.registerBranch(on, 0.1f, (leverBlockEntity, state) -> leverBlockEntity.getBlockState().getValue(LeverBlock.POWERED));
		on.registerBranch(root, 0.1f, (leverBlockEntity, state) -> !leverBlockEntity.getBlockState().getValue(LeverBlock.POWERED));
		return root;
	}

	@Override
	public void mathAnimate(LeverBlockEntity leverBlockEntity, Armature armature, float partialTick) {
	}
}

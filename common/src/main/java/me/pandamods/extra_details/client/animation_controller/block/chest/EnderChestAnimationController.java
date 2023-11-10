package me.pandamods.extra_details.client.animation_controller.block.chest;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.impl.IChest;
import me.pandamods.pandalib.client.animation_controller.Animation;
import me.pandamods.pandalib.client.animation_controller.AnimationController;
import me.pandamods.pandalib.client.model.Armature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;

public class EnderChestAnimationController extends AnimationController<EnderChestBlockEntity> {
	public EnderChestAnimationController(EnderChestBlockEntity base) {
		super(base);
		this.skipAnimation();
		this.setTransitionLength(0.1f);
	}

	private final Animation open = Animation.of(new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/chest/chest_open.json"));
	private final Animation close = Animation.of(new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/chest/chest_close.json"));

	@Override
	public Animation controller(EnderChestBlockEntity base, Armature armature, float deltaSeconds) {
		if (base instanceof IChest chest) {
			return chest.extraDetails$getChestLidController().shouldBeOpen ? this.open : this.close;
		}
		return close;
	}
}

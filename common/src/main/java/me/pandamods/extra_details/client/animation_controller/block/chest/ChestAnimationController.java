package me.pandamods.extra_details.client.animation_controller.block.chest;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.impl.IChest;
import me.pandamods.pandalib.client.animation_controller.Animation;
import me.pandamods.pandalib.client.animation_controller.AnimationController;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.minecraft.resources.ResourceLocation;

public class ChestAnimationController<T extends MeshAnimatable> extends AnimationController<T> {
	public ChestAnimationController(T base) {
		super(base);
		this.skipAnimation();
		this.setTransitionLength(0.1f);
		this.setAnimationSpeed(ExtraDetails.getConfig().blockSettings.chest.animationSpeed);
	}

	private final Animation open = Animation.of(new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/chest/chest_open.json"));
	private final Animation close = Animation.of(new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/chest/chest_close.json"));

	@Override
	public Animation controller(T base, Armature armature, float deltaSeconds) {
		if (base instanceof IChest chest) {
			return chest.extraDetails$getChestLidController().shouldBeOpen ? this.open : this.close;
		}
		return close;
	}
}

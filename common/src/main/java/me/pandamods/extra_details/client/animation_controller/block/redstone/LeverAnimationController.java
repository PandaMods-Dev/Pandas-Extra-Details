package me.pandamods.extra_details.client.animation_controller.block.redstone;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.LeverClientBlock;
import me.pandamods.extra_details.pandalib.client.animation_controller.AnimationController;
import me.pandamods.extra_details.pandalib.client.animation_controller.Animation;
import me.pandamods.extra_details.pandalib.client.model.Armature;
import me.pandamods.extra_details.pandalib.entity.MeshAnimatable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LeverBlock;

public class LeverAnimationController extends AnimationController<LeverClientBlock> {
	public <T extends MeshAnimatable> LeverAnimationController(LeverClientBlock base) {
		super(base);
		this.skipAnimation();
		this.setTransitionLength(0.1f);
		this.setAnimationSpeed(ExtraDetails.getConfig().blockSettings.lever.animationSpeed);
	}

	private final Animation turnOn = Animation.of(new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/lever/lever_on.json"));
	private final Animation turnOff = Animation.of(new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/lever/lever_off.json"));

	@Override
	public Animation controller(LeverClientBlock base, Armature armature, float deltaSeconds) {
		return base.getBlockState().getValue(LeverBlock.POWERED) ? this.turnOn : this.turnOff;
	}
}

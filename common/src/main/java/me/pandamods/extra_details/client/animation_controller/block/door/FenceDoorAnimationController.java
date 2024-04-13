package me.pandamods.extra_details.client.animation_controller.block.door;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.FenceGateClientBlock;
import me.pandamods.extra_details.pandalib.client.animation_controller.Animation;
import me.pandamods.extra_details.pandalib.client.animation_controller.AnimationController;
import me.pandamods.extra_details.pandalib.client.model.Armature;
import me.pandamods.extra_details.pandalib.entity.MeshAnimatable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.FenceGateBlock;

public class FenceDoorAnimationController extends AnimationController<FenceGateClientBlock> {
	public <T extends MeshAnimatable> FenceDoorAnimationController(FenceGateClientBlock base) {
		super(base);
		this.skipAnimation();
		this.setTransitionLength(0.1f);
		this.setAnimationSpeed(ExtraDetails.getConfig().blockSettings.fenceGate.animationSpeed);
	}

	private final Animation open = Animation.of(
			new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/fence_gate/fence_gate_open.json"));
	private final Animation close = Animation.of(
			new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/fence_gate/fence_gate_close.json"));

	@Override
	public Animation controller(FenceGateClientBlock base, Armature armature, float deltaSeconds) {
		return base.getBlockState().getValue(FenceGateBlock.OPEN) ? this.open : this.close;
	}
}

package me.pandamods.extra_details.client.animation_controller.block.door;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.TrapDoorClientBlock;
import me.pandamods.extra_details.pandalib.client.animation_controller.Animation;
import me.pandamods.extra_details.pandalib.client.animation_controller.AnimationController;
import me.pandamods.extra_details.pandalib.client.model.Armature;
import me.pandamods.extra_details.pandalib.entity.MeshAnimatable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.Half;

public class TrapDoorAnimationController extends AnimationController<TrapDoorClientBlock> {
	public <T extends MeshAnimatable> TrapDoorAnimationController(TrapDoorClientBlock base) {
		super(base);
		this.skipAnimation();
		this.setTransitionLength(0.1f);
		this.setAnimationSpeed(ExtraDetails.getConfig().blockSettings.trapdoor.animationSpeed);
	}

	private final Animation lowerOpen = Animation.of(
			new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/trapdoor/trap_door_lower_open.json"));
	private final Animation lowerClose = Animation.of(
			new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/trapdoor/trap_door_lower_close.json"));
	private final Animation upperOpen = Animation.of(
			new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/trapdoor/trap_door_upper_open.json"));
	private final Animation upperClose = Animation.of(
			new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/block/trapdoor/trap_door_upper_close.json"));

	@Override
	public Animation controller(TrapDoorClientBlock base, Armature armature, float deltaSeconds) {
		if (base.getBlockState().getValue(TrapDoorBlock.HALF).equals(Half.TOP)) {
			return base.getBlockState().getValue(TrapDoorBlock.OPEN) ? this.upperOpen : this.upperClose;
		}
		return base.getBlockState().getValue(TrapDoorBlock.OPEN) ? this.lowerOpen : this.lowerClose;
	}
}

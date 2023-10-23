package me.pandamods.extra_details.client.animation_controller.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.LeverClientBlock;
import me.pandamods.pandalib.client.animation_controller.AnimationController;
import me.pandamods.pandalib.client.animation_controller.Animation;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class LeverAnimationController extends AnimationController<LeverClientBlock> {
	public <T extends MeshAnimatable> LeverAnimationController(LeverClientBlock base) {
		super(base);
	}

	private final Animation turnOn = Animation.of(new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/lever_on.json"));
	private final Animation turnOff = Animation.of(new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/lever_off.json"));
	private final Animation turnOffWall = Animation.of(new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/animations/lever_off_wall.json"));

	@Override
	public Animation controller(LeverClientBlock base, Armature armature, float deltaSeconds) {
		if (base.getBlockState().getValue(LeverBlock.FACE).equals(AttachFace.WALL)) {
			return base.getBlockState().getValue(LeverBlock.POWERED) ? this.turnOn : this.turnOffWall;
		}
		return base.getBlockState().getValue(LeverBlock.POWERED) ? this.turnOn : this.turnOff;
	}
}

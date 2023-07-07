package me.pandamods.extra_details.entity.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.ExtraDetailsConfig;
import me.pandamods.extra_details.registries.BlockEntityRegistry;
import me.pandamods.extra_details.utils.animation.Animations;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FenceGateEntity extends BlockEntity implements GeoBlockEntity {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	public FenceGateEntity(BlockPos blockPos, BlockState blockState) {
		super(BlockEntityRegistry.FENCE_GATE_ENTITY.get(), blockPos, blockState);
	}

	public final AnimationController<FenceGateEntity> animationController = new AnimationController<>(this, 0, this::animateController);

	private PlayState animateController(AnimationState state) {
		state.setControllerSpeed(ExtraDetails.getConfig().fence_gate_animation_speed);
		return state.setAndContinue(this.getBlockState().getValue(FenceGateBlock.OPEN) ? Animations.OPEN : Animations.CLOSE);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
		controllerRegistrar.add(animationController);
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}

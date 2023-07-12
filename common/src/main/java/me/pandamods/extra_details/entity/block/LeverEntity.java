package me.pandamods.extra_details.entity.block;

import me.pandamods.extra_details.registries.BlockEntityRegistry;
import me.pandamods.extra_details.utils.animation.Animations;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LeverEntity extends BlockEntity implements GeoBlockEntity {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

	public LeverEntity(BlockPos blockPos, BlockState blockState) {
		super(BlockEntityRegistry.LEVER_ENTITY.get(), blockPos, blockState);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
		controllerRegistrar.add(new AnimationController<>(this, 0, state ->
				state.setAndContinue(this.getBlockState().getValue(LeverBlock.POWERED) ? Animations.ON : Animations.OFF)));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}

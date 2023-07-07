package me.pandamods.extra_details.mixin;

import me.pandamods.extra_details.entity.BetterEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin {
	@Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
	public void isValid(BlockState state, CallbackInfoReturnable<Boolean> cir) {
		if (state.getBlock() instanceof BetterEntityBlock betterEntityBlock) {
			cir.setReturnValue(betterEntityBlock.validBlockClass().isInstance(state.getBlock()));
		}
	}
}

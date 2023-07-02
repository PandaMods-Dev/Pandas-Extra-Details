package me.pandamods.extra_details.mixin.block;

import me.pandamods.extra_details.registries.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TrapDoorBlock.class)
public abstract class TrapDoorMixin extends Block implements EntityBlock {
	public TrapDoorMixin(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull RenderShape getRenderShape(BlockState blockState) {
		return RenderShape.INVISIBLE;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return BlockEntityRegistry.TRAP_DOOR_ENTITY.get().create(blockPos, blockState);
	}
}

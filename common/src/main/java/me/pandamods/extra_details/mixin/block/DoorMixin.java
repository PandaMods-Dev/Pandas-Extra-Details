package me.pandamods.extra_details.mixin.block;

import me.pandamods.extra_details.entity.BetterEntityBlock;
import me.pandamods.extra_details.registries.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DoorBlock.class)
public abstract class DoorMixin extends Block implements BetterEntityBlock {
	public DoorMixin(Properties properties) {
		super(properties);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return BlockEntityRegistry.DOOR_ENTITY.get().create(pos, state);
	}

	@Override
	public Class<?> validBlockClass() {
		return DoorBlock.class;
	}
}

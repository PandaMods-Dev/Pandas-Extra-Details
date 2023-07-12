package me.pandamods.extra_details.mixin.block;

import me.pandamods.extra_details.entity.BetterEntityBlock;
import me.pandamods.extra_details.registries.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LeverBlock.class)
public abstract class LeverMixin extends HorizontalDirectionalBlock implements BetterEntityBlock {
	public LeverMixin(Properties properties) {
		super(properties);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return BlockEntityRegistry.LEVER_ENTITY.get().create(pos, state);
	}

	@Override
	public Class<?> validBlockClass() {
		return LeverBlock.class;
	}
}

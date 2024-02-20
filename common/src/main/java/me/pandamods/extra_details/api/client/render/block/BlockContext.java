package me.pandamods.extra_details.api.client.render.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record BlockContext(
		BlockPos blockPos,
		Level level
) {
	public BlockState blockState() {
		return level.getBlockState(blockPos);
	}
}
package me.pandamods.extra_details.utils.forge;

import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EDSodiumPlatformImpl {
	public static BlockState getBlockState(WorldSlice slice, BlockPos blockPos) {
		return slice.getBlockState(blockPos);
	}
}

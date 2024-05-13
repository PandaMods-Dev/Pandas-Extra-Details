package me.pandamods.extra_details.utils;

import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import dev.architectury.injectables.annotations.ExpectPlatform;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EDSodiumPlatform {
	@ExpectPlatform
	public static BlockState getBlockState(WorldSlice slice, BlockPos blockPos) {
		return null;
	}
}

package me.pandamods.extra_details.api.extensions;

import me.pandamods.extra_details.api.blockdata.BlockData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;

public interface LevelExtension {
	default <T extends BlockData> T extraDetails$getBlockData(BlockPos blockPos, BiFunction<BlockPos, Level, T> newDataProvider) {
		return null;
	}
}

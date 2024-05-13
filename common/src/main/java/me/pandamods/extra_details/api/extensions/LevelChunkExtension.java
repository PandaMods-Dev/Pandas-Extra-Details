package me.pandamods.extra_details.api.extensions;

import me.pandamods.extra_details.api.blockdata.BlockData;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

public interface LevelChunkExtension {
	default Map<BlockPos, BlockData> extraDetails$getBlockDataMap() {
		return new HashMap<>();
	}
}

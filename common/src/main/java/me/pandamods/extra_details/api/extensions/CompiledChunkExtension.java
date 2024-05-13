package me.pandamods.extra_details.api.extensions;

import net.minecraft.core.BlockPos;

import java.util.HashSet;
import java.util.Set;

public interface CompiledChunkExtension {
    default Set<BlockPos> getRenderableBlocks() {
		return new HashSet<>();
	}
}
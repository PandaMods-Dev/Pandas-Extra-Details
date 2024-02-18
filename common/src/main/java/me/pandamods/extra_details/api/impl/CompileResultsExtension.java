package me.pandamods.extra_details.api.impl;

import net.minecraft.core.BlockPos;

import java.util.Set;

public interface CompileResultsExtension {
    Set<BlockPos> getRenderableBlocks();
}
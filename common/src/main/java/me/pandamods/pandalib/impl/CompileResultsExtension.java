package me.pandamods.pandalib.impl;

import me.pandamods.pandalib.client.render.block.ClientBlock;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.Set;

public interface CompileResultsExtension {
    Set<BlockPos> getBlocks();
}
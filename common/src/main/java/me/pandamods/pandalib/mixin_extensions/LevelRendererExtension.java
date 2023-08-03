package me.pandamods.pandalib.mixin_extensions;

import net.minecraft.core.BlockPos;

import java.util.stream.Stream;

public interface LevelRendererExtension {
    Stream<BlockPos> getBlockRenderers();
}
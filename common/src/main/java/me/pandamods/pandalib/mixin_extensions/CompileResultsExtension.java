package me.pandamods.pandalib.mixin_extensions;

import me.pandamods.pandalib.client.render.block.ClientBlock;
import net.minecraft.core.BlockPos;

import java.util.List;

public interface CompileResultsExtension {
    List<ClientBlock> getBlocks();
}
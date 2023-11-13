package me.pandamods.extra_details.mixin.pandalib.client;

import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.impl.CompileResultsExtension;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(ChunkRenderDispatcher.CompiledChunk.class)
public class CompiledChunkMixin implements CompileResultsExtension {
	@Unique
	private Set<BlockPos> blocks = new HashSet<>();

	@Override
	public Set<BlockPos> getBlocks() {
		return blocks;
	}
}

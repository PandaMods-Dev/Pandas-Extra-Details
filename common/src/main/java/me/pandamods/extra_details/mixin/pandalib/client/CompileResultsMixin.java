package me.pandamods.extra_details.mixin.pandalib.client;

import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.mixin_extensions.CompileResultsExtension;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults.class)
public class CompileResultsMixin implements CompileResultsExtension {
	@Unique
	private List<ClientBlock> blocks = new ArrayList<>();

	@Override
	public List<ClientBlock> getBlocks() {
		return blocks;
	}
}

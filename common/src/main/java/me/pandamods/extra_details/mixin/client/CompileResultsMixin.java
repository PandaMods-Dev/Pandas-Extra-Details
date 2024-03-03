package me.pandamods.extra_details.mixin.client;

import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.impl.CompileResultsExtension;
import me.pandamods.extra_details.api.impl.CompiledChunkExtension;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults.class)
public class CompileResultsMixin implements CompileResultsExtension {
	@Unique
	private List<ClientBlockEntity> clientBlockEntities = new ArrayList<>();

	@Override
	public List<ClientBlockEntity> getClientBlockEntities() {
		return clientBlockEntities;
	}
}

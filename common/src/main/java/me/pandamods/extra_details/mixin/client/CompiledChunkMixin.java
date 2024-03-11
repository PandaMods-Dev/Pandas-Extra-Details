package me.pandamods.extra_details.mixin.client;

import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.impl.CompiledChunkExtension;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(ChunkRenderDispatcher.CompiledChunk.class)
public class CompiledChunkMixin implements CompiledChunkExtension {
	@Unique
	private List<ClientBlockEntity> clientBlockEntities = new ArrayList<>();

	@Override
	public List<ClientBlockEntity> getClientBlockEntities() {
		return clientBlockEntities;
	}
}

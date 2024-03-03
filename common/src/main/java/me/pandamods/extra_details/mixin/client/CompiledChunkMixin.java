package me.pandamods.extra_details.mixin.client;

import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.impl.CompiledChunkExtension;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(ChunkRenderDispatcher.CompiledChunk.class)
public class CompiledChunkMixin implements CompiledChunkExtension {
	@Unique
	private List<ClientBlockEntity> clientBlockEntities = new ArrayList<>();

	@Override
	public List<ClientBlockEntity> getClientBlockEntities() {
		return clientBlockEntities;
	}
}

package me.pandamods.extra_details.mixin.pandalib.client.sodium;

import me.jellysquid.mods.sodium.client.render.chunk.data.ChunkRenderData;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.mixin_extensions.CompileResultsExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Environment(EnvType.CLIENT)
@Mixin(ChunkRenderData.class)
public class ChunkRenderDataMixin implements CompileResultsExtension {
	@Unique
    private final List<ClientBlock> clientBlock = new ArrayList<>();

	@Override
	public List<ClientBlock> getBlocks() {
		return clientBlock;
	}
}

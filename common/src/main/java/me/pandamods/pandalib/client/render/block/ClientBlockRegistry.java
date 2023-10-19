package me.pandamods.pandalib.client.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;

import java.util.*;

public class ClientBlockRegistry {
    public static final Map<ResourceLocation, ClientBlockType<?>> BLOCK_TYPES = new HashMap<>();

    public static <T extends ClientBlock> ClientBlockType<T> register(ResourceLocation resourceLocation, ClientBlockType<T> blockRenderType) {
		blockRenderType.name = resourceLocation;
        BLOCK_TYPES.put(resourceLocation, blockRenderType);
		return blockRenderType;
    }

	public static ClientBlockProvider get(Block block) {
		ClientBlockType<?> type = getType(block);

		if (type == null)
			return null;
		return type.provider;
    }

	public static ClientBlockType<?> getType(Block block) {
		List<ClientBlockType<?>> list = BLOCK_TYPES.values().stream().filter(renderType ->
				renderType.blockTags.stream().anyMatch(tag -> block.defaultBlockState().is(tag)) ||
						renderType.blocks != null && renderType.blocks.contains(block)).toList();
		if (list.isEmpty())
			return null;
		return list.get(0);
    }
}
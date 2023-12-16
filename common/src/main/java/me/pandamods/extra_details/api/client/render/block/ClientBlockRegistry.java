package me.pandamods.extra_details.api.client.render.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientBlockRegistry {
    public static final Map<ResourceLocation, ClientBlockType<?>> BLOCK_TYPES = new HashMap<>();

    public static <T extends ClientBlock> ClientBlockType<T> register(ResourceLocation resourceLocation, ClientBlockType<T> blockRenderType) {
		blockRenderType.name = resourceLocation;
        BLOCK_TYPES.put(resourceLocation, blockRenderType);
		return blockRenderType;
    }

	public static ClientBlockProvider get(Block block) {
		return getType(block).map(clientBlockType -> clientBlockType.provider).orElse(null);
	}

	public static <T extends ClientBlock> Optional<ClientBlockType<?>> getType(Block block) {
		return BLOCK_TYPES.values().stream().filter(renderType -> renderType.isValid(block.defaultBlockState())).findFirst();
    }
}
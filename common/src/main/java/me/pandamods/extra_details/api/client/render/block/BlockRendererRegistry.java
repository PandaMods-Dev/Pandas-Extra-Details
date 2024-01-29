package me.pandamods.extra_details.api.client.render.block;

import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class BlockRendererRegistry {
    public static final Map<ClientBlockType<?>, ClientBlockRenderer<?>> RENDERERS = new HashMap<>();

    public static <T extends ClientBlock> void register(ClientBlockType<T> clientBlockType, ClientBlockRenderer<T> clientBlockRenderer) {
        RENDERERS.put(clientBlockType, clientBlockRenderer);
    }

	@SuppressWarnings("unchecked")
	public static <T extends ClientBlock> ClientBlockRenderer<T> get(Block block) {
		Optional<ClientBlockType<?>> type = ClientBlockRegistry.getType(block);
		return (ClientBlockRenderer<T>) type.map(BlockRendererRegistry::get).orElse(null);
    }

	@SuppressWarnings("unchecked")
	public static <T extends ClientBlock> ClientBlockRenderer<T> get(ClientBlockType<T> clientBlockType) {
		return (ClientBlockRenderer<T>) RENDERERS.get(clientBlockType);
    }
}
package me.pandamods.pandalib.client.render.block;

import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BlockRendererRegistry {
    public static final Map<ClientBlockType<?>, ClientBlockRenderer<?>> RENDERERS = new HashMap<>();

    public static <T extends ClientBlock> void register(ClientBlockType<T> clientBlockType, ClientBlockRenderer<T> clientBlockRenderer) {
        RENDERERS.put(clientBlockType, clientBlockRenderer);
    }

	public static <T extends ClientBlock> ClientBlockRenderer<T> get(Block block) {
		return get(ClientBlockRegistry.getType(block));
    }

	public static <T extends ClientBlock> ClientBlockRenderer<T> get(ClientBlockType<?> clientBlockType) {
		return (ClientBlockRenderer<T>) RENDERERS.get(clientBlockType);
    }
}
package me.pandamods.extra_details.api.render;

import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BlockRendererRegistry {
	private static final Map<Block, BlockRenderer> RENDERERS = new HashMap<>();

	public static BlockRenderer get(Block block) {
		return RENDERERS.get(block);
	}

	public static void register(Block block, BlockRenderer blockRenderer) {
		RENDERERS.put(block, blockRenderer);
	}
}

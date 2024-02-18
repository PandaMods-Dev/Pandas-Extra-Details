package me.pandamods.extra_details.api.client.render.block;

import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class BlockRendererRegistry {
	protected Map<Block, BlockRenderer> renderers = new HashMap<>();

	public void register(Block block, BlockRenderer renderer) {
		renderers.put(block, renderer);
	}
}
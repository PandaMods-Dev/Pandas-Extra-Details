package me.pandamods.extra_details.api.client.render.block;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Function;

public class BlockRendererRegistry {
    public static final Map<Block, BlockRendererProvider> RENDERERS = new HashMap<>();

	public static void register(Block block, BlockRendererProvider provider) {
		RENDERERS.put(block, provider);
	}

	public static BlockRendererProvider get(Block block) {
		return RENDERERS.get(block);
	}
}
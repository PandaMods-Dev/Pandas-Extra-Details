package me.pandamods.extra_details.api.client.render.block;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Function;

public class BlockRendererRegistry {
    public static final Map<Function<BlockState, Boolean>, BlockRendererProvider> RENDERERS = new HashMap<>();

	public static void register(Block block, BlockRendererProvider provider) {
		register(blockState -> blockState.is(block), provider);
	}

	public static void register(TagKey<Block> tag, BlockRendererProvider provider) {
		register(blockState -> blockState.is(tag), provider);
	}

	public static void register(Function<BlockState, Boolean> prediction, BlockRendererProvider provider) {
		RENDERERS.put(prediction, provider);
	}

	public static List<BlockRendererProvider> get(BlockState blockState) {
		return RENDERERS.entrySet().stream().filter(entry -> entry.getKey().apply(blockState)).map(Map.Entry::getValue).toList();
	}

	public static Optional<BlockRendererProvider> getFirst(BlockState blockState) {
		return Optional.ofNullable(get(blockState).get(0));
	}
}
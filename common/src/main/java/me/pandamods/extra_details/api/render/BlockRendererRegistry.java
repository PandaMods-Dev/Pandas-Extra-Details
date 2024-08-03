/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.extra_details.api.render;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockRendererRegistry {
	private static final List<Pair<Function<BlockState, Boolean>, BlockRenderer>> RENDERERS = new ObjectArrayList<>();

	public static BlockRenderer get(BlockState blockState) {
		for (Pair<Function<BlockState, Boolean>, BlockRenderer> renderer : RENDERERS) {
			if (renderer.getA().apply(blockState))
				return renderer.getB();
		}
		return null;
	}

	public static void register(BlockRenderer blockRenderer, Function<BlockState, Boolean> condition) {
		RENDERERS.add(new Pair<>(condition, blockRenderer));
	}

	public static void register(BlockRenderer blockRenderer, Block block) {
		register(blockRenderer, blockState -> blockState.is(block));
	}

	public static void register(BlockRenderer blockRenderer, Block block, Function<BlockState, Boolean> condition) {
		register(blockRenderer, blockState -> blockState.is(block) && condition.apply(blockState));
	}

	public static void register(BlockRenderer blockRenderer, Block... blocks) {
		for (Block block : blocks) {
			register(blockRenderer, blockState -> blockState.is(block));
		}
	}

	public static void register(BlockRenderer blockRenderer, Function<BlockState, Boolean> condition, Block... blocks) {
		for (Block block : blocks) {
			register(blockRenderer, blockState -> blockState.is(block) && condition.apply(blockState));
		}
	}

	public static void register(BlockRenderer blockRenderer, TagKey<Block> blockTag) {
		register(blockRenderer, blockState -> blockState.is(blockTag));
	}

	public static void register(BlockRenderer blockRenderer, TagKey<Block> blockTag, Function<BlockState, Boolean> condition) {
		register(blockRenderer, blockState -> blockState.is(blockTag) && condition.apply(blockState));
	}
}

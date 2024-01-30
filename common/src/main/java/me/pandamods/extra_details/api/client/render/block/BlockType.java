package me.pandamods.extra_details.api.client.render.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class BlockType {
	private final BlockRendererProvider provider;
	public final List<Block> blocks;
	public final List<TagKey<Block>> tags;
	private final ResourceLocation registryName;

	public BlockType(BlockRendererProvider provider, List<Block> blocks, List<TagKey<Block>> tags, ResourceLocation registryName) {
		this.provider = provider;
		this.blocks = blocks;
		this.tags = tags;
		this.registryName = registryName;
	}
}

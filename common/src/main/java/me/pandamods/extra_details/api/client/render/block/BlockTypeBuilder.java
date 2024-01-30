package me.pandamods.extra_details.api.client.render.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockTypeBuilder {
	private final BlockRendererProvider provider;
	private List<Block> blocks = new ArrayList<>();
	private List<TagKey<Block>> tags = new ArrayList<>();

	public BlockTypeBuilder(BlockRendererProvider provider) {
		this.provider = provider;
	}

	public BlockTypeBuilder validBlocks(Block... blocks) {
		this.blocks = new ArrayList<>(List.of(blocks));
		return this;
	}

	@SafeVarargs
	public final BlockTypeBuilder validTags(TagKey<Block>... blockTags) {
		this.tags = new ArrayList<>(List.of(blockTags));
		return this;
	}

	BlockType build(ResourceLocation registryName) {
		return new BlockType(provider, blocks, tags, registryName);
	}
}

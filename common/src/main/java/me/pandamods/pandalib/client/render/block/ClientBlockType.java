package me.pandamods.pandalib.client.render.block;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

public class ClientBlockType<T extends ClientBlock> {
	public final ClientBlockProvider provider;
	public final List<Block> blocks;
	public final List<BlockTags> blockTags;

	public ClientBlockType(ClientBlockProvider provider, List<Block> blocks, List<BlockTags> blockTags) {
		this.provider = provider;
		this.blocks = blocks;
		this.blockTags = blockTags;
	}

	public static class Builder<T extends ClientBlock> {
		private final ClientBlockProvider provider;
		private List<Block> blocks;
		private List<BlockTags> blockTags;

		public Builder(ClientBlockProvider provider) {
			this.provider = provider;
		}

		public Builder<T> validBlocks(Block... blocks) {
			this.blocks = new ArrayList<>(List.of(blocks));
			return this;
		}

		public Builder<T> validBlockTags(BlockTags... blockTags) {
			this.blockTags = new ArrayList<>(List.of(blockTags));
			return this;
		}

		public ClientBlockType<T> build() {
			return new ClientBlockType<>(provider, blocks, blockTags);
		}
	}
}

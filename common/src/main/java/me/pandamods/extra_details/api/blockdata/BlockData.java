package me.pandamods.extra_details.api.blockdata;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

@Environment(EnvType.CLIENT)
public class BlockData {
	private final BlockPos blockPos;
	private final Level level;
	private final Block block;

	public BlockData(BlockPos blockPos, Level level) {
		this.blockPos = blockPos;
		this.level = level;
		this.block = level.getBlockState(blockPos).getBlock();
	}

	public boolean validate() {
		return this.level.getBlockState(this.blockPos).is(this.block);
	}
}

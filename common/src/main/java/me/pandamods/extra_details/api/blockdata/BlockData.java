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

package me.pandamods.extra_details.api.blockdata;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

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
		return validate(getBlockstate());
	}

	public boolean validate(BlockState blockState) {
		return blockState.is(this.block);
	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	public Level getLevel() {
		return level;
	}

	public BlockState getBlockstate() {
		return this.getLevel().getBlockState(this.getBlockPos());
	}
}

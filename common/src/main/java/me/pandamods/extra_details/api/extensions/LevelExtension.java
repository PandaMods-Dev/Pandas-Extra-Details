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

package me.pandamods.extra_details.api.extensions;

import me.pandamods.extra_details.api.blockdata.BlockData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;

public interface LevelExtension {
	default <T extends BlockData> T extraDetails$getBlockData(BlockPos blockPos, BiFunction<BlockPos, Level, T> newDataProvider) {
		return null;
	}
}

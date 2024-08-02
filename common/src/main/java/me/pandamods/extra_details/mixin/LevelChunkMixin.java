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

package me.pandamods.extra_details.mixin;

import me.pandamods.extra_details.api.blockdata.BlockData;
import me.pandamods.extra_details.api.extensions.LevelChunkExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin implements LevelChunkExtension {
	@Unique
	private final Map<BlockPos, BlockData> extraDetails$blockDataMap = new HashMap<>();

	@Override
	public Map<BlockPos, BlockData> extraDetails$getBlockDataMap() {
		return extraDetails$blockDataMap;
	}

	@Inject(method = "setBlockState", at = @At("RETURN"))
	public void setBlockState(BlockPos pos, BlockState state, boolean isMoving, CallbackInfoReturnable<BlockState> cir) {
		BlockData blockData = extraDetails$getBlockDataMap().get(pos);
		if (blockData != null && !blockData.validate(state))
			extraDetails$getBlockDataMap().remove(pos);
	}
}

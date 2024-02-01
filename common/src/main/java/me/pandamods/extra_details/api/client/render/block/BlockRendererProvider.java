package me.pandamods.extra_details.api.client.render.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public interface BlockRendererProvider {
	BlockRenderer create(Block block, ClientLevel level, BlockPos blockPos);
}

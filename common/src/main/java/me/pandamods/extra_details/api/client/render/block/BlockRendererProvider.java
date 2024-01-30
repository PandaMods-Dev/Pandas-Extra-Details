package me.pandamods.extra_details.api.client.render.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;

public interface BlockRendererProvider {
	BlockRenderer create(ClientLevel level, BlockPos blockPos);
}

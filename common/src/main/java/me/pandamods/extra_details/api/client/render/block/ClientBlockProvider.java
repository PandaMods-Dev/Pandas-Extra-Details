package me.pandamods.extra_details.api.client.render.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface ClientBlockProvider {
	ClientBlock create(ClientBlockType<?> type, BlockPos blockPos, ClientLevel level);
}

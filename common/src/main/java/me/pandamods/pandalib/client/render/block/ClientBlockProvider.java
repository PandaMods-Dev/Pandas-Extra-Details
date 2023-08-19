package me.pandamods.pandalib.client.render.block;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface ClientBlockProvider {
	ClientBlock create(BlockPos blockPos, BlockState blockState, ClientLevel level);
}

package me.pandamods.extra_details.client.clientblockentity;

import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.registries.ClientBlockEntityRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class LeverBlockEntity extends ClientBlockEntity {
	public LeverBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ClientBlockEntityRegistries.LEVER, blockPos, blockState);
	}
}

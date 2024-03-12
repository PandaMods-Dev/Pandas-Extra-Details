package me.pandamods.extra_details.client.clientblockentity;

import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntityType;
import me.pandamods.extra_details.api.clientblockentity.MeshClientBlockEntity;
import me.pandamods.extra_details.registries.ClientBlockEntityRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DoorBlockEntity extends MeshClientBlockEntity {
	public DoorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ClientBlockEntityRegistries.DOOR, blockPos, blockState);
	}
}

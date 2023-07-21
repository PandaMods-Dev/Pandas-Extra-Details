package me.pandamods.extra_details.entity.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.registries.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class DoorEntity extends BlockEntity {
	public float openingTime;

	public DoorEntity(BlockPos blockPos, BlockState blockState) {
		super(BlockEntityRegistry.DOOR_ENTITY.get(), blockPos, blockState);
		openingTime = blockState.getValue(DoorBlock.OPEN) ? 1 : 0;
		ExtraDetails.LOGGER.error("created entity");
	}
}

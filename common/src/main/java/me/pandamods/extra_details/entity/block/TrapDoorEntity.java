package me.pandamods.extra_details.entity.block;

import me.pandamods.extra_details.registries.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TrapDoorEntity extends BlockEntity {
	public float openingTime;
	public TrapDoorEntity(BlockPos blockPos, BlockState blockState) {
		super(BlockEntityRegistry.TRAP_DOOR_ENTITY.get(), blockPos, blockState);
		openingTime = blockState.getValue(TrapDoorBlock.OPEN) ? 1 : 0;
	}
}

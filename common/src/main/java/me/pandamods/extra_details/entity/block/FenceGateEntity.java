package me.pandamods.extra_details.entity.block;

import me.pandamods.extra_details.registries.BlockEntityRegistry;
import me.pandamods.pandalib.cache.MeshCache;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FenceGateEntity extends BlockEntity implements MeshAnimatable {
	private final MeshCache cache = new MeshCache();
	public float openingTime;

	public FenceGateEntity(BlockPos blockPos, BlockState blockState) {
		super(BlockEntityRegistry.FENCE_GATE_ENTITY.get(), blockPos, blockState);
		openingTime = blockState.getValue(FenceGateBlock.OPEN) ? 1 : 0;
	}

	@Override
	public MeshCache getCache() {
		return this.cache;
	}
}

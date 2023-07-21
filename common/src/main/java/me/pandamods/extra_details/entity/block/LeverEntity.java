package me.pandamods.extra_details.entity.block;

import me.pandamods.extra_details.registries.BlockEntityRegistry;
import me.pandamods.pandalib.cache.MeshCache;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LeverEntity extends BlockEntity implements MeshAnimatable {
	private final MeshCache cache = new MeshCache();
	public float animTime;

	public LeverEntity(BlockPos blockPos, BlockState blockState) {
		super(BlockEntityRegistry.LEVER_ENTITY.get(), blockPos, blockState);
		animTime = blockState.getValue(LeverBlock.POWERED) ? 1 : 0;
	}

	@Override
	public MeshCache getCache() {
		return this.cache;
	}
}

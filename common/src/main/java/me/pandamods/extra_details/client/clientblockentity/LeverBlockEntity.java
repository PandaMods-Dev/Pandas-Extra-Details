package me.pandamods.extra_details.client.clientblockentity;

import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.registries.ClientBlockEntityRegistries;
import me.pandamods.pandalib.client.armature.AnimatableCache;
import me.pandamods.pandalib.client.armature.IAnimatable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class LeverBlockEntity extends ClientBlockEntity implements IAnimatable {
	private AnimatableCache animatableCache = new AnimatableCache();

	public LeverBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ClientBlockEntityRegistries.LEVER, blockPos, blockState);
	}

	@Override
	public AnimatableCache animatableCache() {
		return this.animatableCache;
	}
}

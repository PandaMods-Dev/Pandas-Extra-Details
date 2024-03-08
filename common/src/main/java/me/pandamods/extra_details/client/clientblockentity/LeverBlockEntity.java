package me.pandamods.extra_details.client.clientblockentity;

import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.registries.ClientBlockEntityRegistries;
import me.pandamods.pandalib.client.armature.ArmatureCache;
import me.pandamods.pandalib.client.armature.IArmatureCache;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class LeverBlockEntity extends ClientBlockEntity implements IArmatureCache {
	private ArmatureCache armatureCache = new ArmatureCache();

	public LeverBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ClientBlockEntityRegistries.LEVER, blockPos, blockState);
	}

	@Override
	public ArmatureCache armatureCache() {
		return this.armatureCache;
	}
}

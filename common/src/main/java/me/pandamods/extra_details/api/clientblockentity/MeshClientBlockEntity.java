package me.pandamods.extra_details.api.clientblockentity;

import com.mojang.blaze3d.Blaze3D;
import me.pandamods.pandalib.client.armature.AnimatableCache;
import me.pandamods.pandalib.client.armature.IAnimatable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MeshClientBlockEntity extends ClientBlockEntity implements IAnimatable {
	protected final AnimatableCache animatableCache = new AnimatableCache();

	public MeshClientBlockEntity(ClientBlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
		super(type, blockPos, blockState);
	}

	@Override
	public AnimatableCache animatableCache() {
		return this.animatableCache;
	}

	@Override
	public float getTick(float partialTick) {
		return (float) Blaze3D.getTime() * 20f;
	}
}

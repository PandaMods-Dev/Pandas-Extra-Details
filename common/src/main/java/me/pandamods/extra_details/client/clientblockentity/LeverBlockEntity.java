package me.pandamods.extra_details.client.clientblockentity;

import com.mojang.blaze3d.Blaze3D;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.registries.ClientBlockEntityRegistries;
import me.pandamods.pandalib.client.armature.AnimatableCache;
import me.pandamods.pandalib.client.armature.IAnimatable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;

public class LeverBlockEntity extends ClientBlockEntity implements IAnimatable {
	private final AnimatableCache animatableCache = new AnimatableCache();

	public LeverBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ClientBlockEntityRegistries.LEVER, blockPos, blockState);
	}

	@Override
	public AnimatableCache animatableCache() {
		return this.animatableCache;
	}

	@Override
	public int getTick() {
		return (int) Math.floor(Blaze3D.getTime() * 20);
	}
}

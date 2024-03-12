package me.pandamods.extra_details.client.clientblockentity;

import com.mojang.blaze3d.Blaze3D;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.clientblockentity.MeshClientBlockEntity;
import me.pandamods.extra_details.registries.ClientBlockEntityRegistries;
import me.pandamods.pandalib.client.armature.AnimatableCache;
import me.pandamods.pandalib.client.armature.IAnimatable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;

public class LeverBlockEntity extends MeshClientBlockEntity {
	public LeverBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(ClientBlockEntityRegistries.LEVER, blockPos, blockState);
	}
}

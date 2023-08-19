package me.pandamods.pandalib.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public abstract class ClientBlock {
	private final BlockPos blockPos;
	private BlockState blockState;
	private final ClientLevel level;

	public ClientBlock(BlockPos blockPos, BlockState blockState, ClientLevel level) {
		this.blockPos = blockPos;
		this.blockState = blockState;
		this.level = level;
	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	public BlockState getBlockState() {
		return blockState;
	}
	public void setBlockState(BlockState blockState) {
		this.blockState = blockState;
	}

	public ClientLevel getLevel() {
		return level;
	}
}

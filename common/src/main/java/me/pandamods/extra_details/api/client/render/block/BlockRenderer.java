package me.pandamods.extra_details.api.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockRenderer {
	private final Block block;
	private final ClientLevel level;
	private final BlockPos blockPos;

	public BlockRenderer(Block block, ClientLevel level, BlockPos blockPos) {
		this.block = block;
		this.level = level;
		this.blockPos = blockPos;
	}

	public void render(PoseStack poseStack, MultiBufferSource bufferSource, float partialTick) {}

	public void tick() {}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	public ClientLevel getLevel() {
		return level;
	}

	public BlockState getBlockstate() {
		return getLevel().getBlockState(getBlockPos());
	}

	public Block getBlock() {
		return block;
	}

	public int getLightColor() {
		if (getLevel() != null) {
			return LevelRenderer.getLightColor(level, blockPos);
		} else {
			return  15728880;
		}
	}
}

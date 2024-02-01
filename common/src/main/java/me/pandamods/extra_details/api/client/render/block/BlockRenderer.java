package me.pandamods.extra_details.api.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.ExtraDetails;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;

public abstract class BlockRenderer {
	private final Block block;
	private final ClientLevel level;
	private final BlockPos blockPos;

	public BlockRenderer(Block block, ClientLevel level, BlockPos blockPos) {
		this.block = block;
		this.level = level;
		this.blockPos = blockPos;
	}

	public abstract void render(PoseStack poseStack, MultiBufferSource bufferSource);

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
		if (level != null) {
			return LevelRenderer.getLightColor(level, blockPos);
		} else {
			return  15728880;
		}
	}
}

package me.pandamods.pandalib.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.entity.block.DoorEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockRenderer {
	void render(BlockPos pos, BlockState state, float partialTick, float deltaSeconds, PoseStack poseStack, MultiBufferSource multiBufferSource);
}

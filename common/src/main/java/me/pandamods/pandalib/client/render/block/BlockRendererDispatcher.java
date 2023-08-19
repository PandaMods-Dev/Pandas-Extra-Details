package me.pandamods.pandalib.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockRendererDispatcher {
	public static void render(ClientLevel level, PoseStack poseStack, MultiBufferSource buffer, ClientBlock clientBlock,
							  int lightColor, int overlay, float partialTick) {
		BlockState state = clientBlock.getBlockState();
		BlockRendererRegistry.get(state.getBlock()).render(clientBlock, poseStack, buffer, lightColor, overlay, partialTick);
	}
}

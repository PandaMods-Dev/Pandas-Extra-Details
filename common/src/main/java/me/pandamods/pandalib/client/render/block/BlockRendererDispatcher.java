package me.pandamods.pandalib.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockRendererDispatcher {
	public static Map<BlockPos, BlockState> renderers = new HashMap<>();

	public static void render(ClientLevel level, PoseStack poseStack, MultiBufferSource buffer, Camera camera, BlockRenderer renderers, BlockPos pos,
							  int lightColor, int noOverlay, float partialTick) {

	}
}

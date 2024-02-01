package me.pandamods.extra_details.api.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientBlockRenderDispatcher {
	public static final Map<BlockPos, BlockRenderer> RENDERERS = new HashMap<>();

	public static void render(List<BlockRenderer> renderers, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
		for (BlockRenderer renderer : renderers) {
			renderer.render(poseStack, bufferSource);
		}
	}
}

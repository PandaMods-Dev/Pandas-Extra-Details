package me.pandamods.pandalib.client.render.block;

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

import java.util.HashMap;
import java.util.Map;

public class ClientBlockRenderDispatcher {
	public static final Map<BlockPos, ClientBlock> CLIENT_BLOCKS = new HashMap<>();

	public static <T extends ClientBlock> void render(PoseStack poseStack, MultiBufferSource buffer, T clientBlock, float partialTick) {
		ClientBlockRenderer<T> renderer = BlockRendererRegistry.get(clientBlock.getBlockState().getBlock());

		if (renderer == null || !renderer.enabled(clientBlock.getBlockState())) {
			return;
		}

		if (!renderer.shouldRender(clientBlock, Minecraft.getInstance().getBlockEntityRenderDispatcher().camera.getPosition())) {
			return;
		}

		ClientBlockRenderDispatcher.tryRender(clientBlock, () ->
				ClientBlockRenderDispatcher.setupAndRender(renderer, clientBlock, partialTick, poseStack, buffer));
	}

	private static <T extends ClientBlock> void setupAndRender(ClientBlockRenderer<T> renderer, T clientBlock, float partialTick, PoseStack poseStack,
															   MultiBufferSource bufferSource) {
		Level level = clientBlock.getLevel();
		int lightColor;
		if (level != null) {
			lightColor = LevelRenderer.getLightColor(level, clientBlock.getBlockPos());
		} else {
			lightColor = 15728880;
		}
		renderer.render(clientBlock, poseStack, bufferSource, lightColor, OverlayTexture.NO_OVERLAY, partialTick);
	}

	private static void tryRender(ClientBlock clientBlock, Runnable renderer) {
		try {
			renderer.run();
		} catch (Throwable throwable) {
			CrashReport crashReport = CrashReport.forThrowable(throwable, "Rendering Client Block");
			CrashReportCategory crashReportCategory = crashReport.addCategory("Client Block Details");
			clientBlock.fillCrashReportCategory(crashReportCategory);
			throw new ReportedException(crashReport);
		}
	}
}

package me.pandamods.extra_details.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.api.client.render.block.BlockRenderer;
import me.pandamods.extra_details.client.model.LeverModel;
import me.pandamods.pandalib.client.Model;
import me.pandamods.pandalib.client.geometry.GeoBlockRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class LeverRenderer extends GeoBlockRenderer<LeverModel> {
	public LeverRenderer() {
		super(new LeverModel());
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor) {
		if (true) {
			VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(new ResourceLocation("textures/block/cobblestone.png")));
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 6; j++) {
					vertexConsumer
							.vertex(poseStack.last().pose(), 0, 1, 0)
							.color(255, 255, 255, 255)
							.uv(0, 0)
							.overlayCoords(OverlayTexture.NO_OVERLAY)
							.uv2(lightColor)
							.normal(poseStack.last().normal(), 0, 1, 0)
							.endVertex();
					vertexConsumer
							.vertex(poseStack.last().pose(), 0, 1, 1)
							.color(255, 255, 255, 255)
							.uv(0, 1)
							.overlayCoords(OverlayTexture.NO_OVERLAY)
							.uv2(lightColor)
							.normal(poseStack.last().normal(), 0, 1, 0)
							.endVertex();
					vertexConsumer
							.vertex(poseStack.last().pose(), 1, 1, 1)
							.color(255, 255, 255, 255)
							.uv(1, 1)
							.overlayCoords(OverlayTexture.NO_OVERLAY)
							.uv2(lightColor)
							.normal(poseStack.last().normal(), 0, 1, 0)
							.endVertex();
					vertexConsumer
							.vertex(poseStack.last().pose(), 1, 1, 0)
							.color(255, 255, 255, 255)
							.uv(1, 0)
							.overlayCoords(OverlayTexture.NO_OVERLAY)
							.uv2(lightColor)
							.normal(poseStack.last().normal(), 0, 1, 0)
							.endVertex();
				}
			}
		}
	}
}

package me.pandamods.extra_details.api.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;

public interface BlockRenderer {
	void render(PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor);
}

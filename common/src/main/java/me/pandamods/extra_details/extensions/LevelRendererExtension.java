package me.pandamods.extra_details.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;

public interface LevelRendererExtension {
	void extraDetails$renderBlocks(PoseStack poseStack,
								   MultiBufferSource.BufferSource bufferSource,
								   MultiBufferSource.BufferSource bufferSource2,
								   Camera camera, float partialTick);
}

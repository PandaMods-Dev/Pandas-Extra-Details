package me.pandamods.pandalib.client.geometry;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.api.client.render.block.BlockRenderer;
import me.pandamods.pandalib.client.Model;
import net.minecraft.client.renderer.MultiBufferSource;

public abstract class GeoBlockRenderer<M extends Model> implements BlockRenderer, GeoRenderer {
	public M model;

	public GeoBlockRenderer(M model) {
		this.model = model;
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor) {
	}
}

package me.pandamods.pandalib.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.mixin.renderer.BlockRenderDispatcherAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderUtils {
	public static final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

	public static void renderBlock(PoseStack poseStack, BlockState state, MultiBufferSource multiBufferSource, int overlay, int light) {
		BakedModel bakedModel = blockRenderer.getBlockModel(state);
		int k = ((BlockRenderDispatcherAccessor)blockRenderer).getBlockColors().getColor(state, null, null, 0);
		float r = (float)(k >> 16 & 0xFF) / 255.0f;
		float g = (float)(k >> 8 & 0xFF) / 255.0f;
		float b = (float)(k & 0xFF) / 255.0f;
		blockRenderer.getModelRenderer().renderModel(poseStack.last(),
				multiBufferSource.getBuffer(ItemBlockRenderTypes.getRenderType(state, false)), state, bakedModel, r, g, b, overlay, light);
	}

	public static float getDeltaSeconds() {
		return 1 / ((float) Minecraft.getInstance().getFps());
	}

	public static List<ResourceLocation> getBlockTextures(BlockState blockState) {
		ModelManager manager = Minecraft.getInstance().getModelManager();
		BakedModel model = manager.getBlockModelShaper().getBlockModel(blockState);
		List<BakedQuad> quads = model.getQuads(null, null, RandomSource.create());
		List<ResourceLocation> textures = new ArrayList<>();
		for (BakedQuad quad : quads) {
			if (!textures.contains(quad.getSprite().contents().name())) {
				textures.add(quad.getSprite().contents().name());
			}
		}
		return textures;
	}
}

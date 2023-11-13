package me.pandamods.pandalib.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.mixin.renderer.BlockRenderDispatcherAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class RenderUtils {
	public static final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

	public static void renderBlock(PoseStack poseStack, BlockState blockState, BlockPos blockPos,
								   Level level, VertexConsumer vertexConsumer) {
		BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
		BakedModel bakedModel = blockRenderDispatcher.getBlockModel(blockState);
		blockRenderDispatcher.getModelRenderer().tesselateBlock(level, bakedModel, blockState, blockPos, poseStack, vertexConsumer,
				false, level.getRandom(), blockState.getSeed(blockPos), OverlayTexture.NO_OVERLAY);
	}

	public static float getDeltaSeconds() {
		return Minecraft.getInstance().getDeltaFrameTime() / 20;
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

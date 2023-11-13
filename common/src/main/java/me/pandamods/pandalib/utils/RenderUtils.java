package me.pandamods.pandalib.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class RenderUtils {
	public static final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

	public static void renderBlock(PoseStack poseStack, BlockState blockState, BlockPos blockPos,
								   Level level, VertexConsumer vertexConsumer, int lightColor, int overlay) {
		render(poseStack, blockState, blockPos, level, vertexConsumer, lightColor, overlay);

		if (blockPos != null) {
			SortedSet<BlockDestructionProgress> sortedSet = Minecraft.getInstance()
					.levelRenderer.destructionProgress.get(blockPos.asLong());
			int progress;
			if (sortedSet != null && !sortedSet.isEmpty() && (progress = sortedSet.last().getProgress()) >= 0) {
				VertexConsumer destroyConsumer = new SheetedDecalTextureGenerator(Minecraft.getInstance().renderBuffers().crumblingBufferSource()
						.getBuffer(ModelBakery.DESTROY_TYPES.get(progress)),
						poseStack.last().pose(), poseStack.last().normal(), 1.0f);
				render(poseStack, blockState, blockPos, level, destroyConsumer, lightColor, overlay);
			}
		}
	}

	private static void render(PoseStack poseStack, BlockState blockState, BlockPos blockPos,
								   Level level, VertexConsumer vertexConsumer, int lightColor, int overlay) {
		BakedModel bakedModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(blockState);
		int i = Minecraft.getInstance().getBlockColors().getColor(blockState, level, blockPos, 0);
		float f = (float)(i >> 16 & 0xFF) / 255.0f;
		float g = (float)(i >> 8 & 0xFF) / 255.0f;
		float h = (float)(i & 0xFF) / 255.0f;
		Minecraft.getInstance().getBlockRenderer().getModelRenderer().renderModel(poseStack.last(),
				vertexConsumer, blockState, bakedModel, f, g, h, lightColor, overlay);
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

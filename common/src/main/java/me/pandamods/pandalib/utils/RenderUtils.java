package me.pandamods.pandalib.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
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
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public class RenderUtils {
	public static final BlockRenderDispatcher blockRenderer = Minecraft.getInstance().getBlockRenderer();

	public static void renderBlock(PoseStack poseStack, BlockState blockState, BlockPos blockPos,
								   Level level, VertexConsumer vertexConsumer) {
		render(poseStack, blockState, blockPos, level, vertexConsumer);

		if (blockPos != null) {
			SortedSet<BlockDestructionProgress> sortedSet = Minecraft.getInstance()
					.levelRenderer.destructionProgress.get(blockPos.asLong());
			int progress;
			if (sortedSet != null && !sortedSet.isEmpty() && (progress = sortedSet.last().getProgress()) >= 0) {
				VertexConsumer destroyConsumer = new SheetedDecalTextureGenerator(Minecraft.getInstance().renderBuffers().crumblingBufferSource()
						.getBuffer(ModelBakery.DESTROY_TYPES.get(progress)),
						poseStack.last().pose(), poseStack.last().normal(), 1.0f);
				render(poseStack, blockState, blockPos, level, destroyConsumer);
			}
		}
	}

	private static void render(PoseStack poseStack, BlockState blockState, BlockPos blockPos,
								   Level level, VertexConsumer vertexConsumer) {
		BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();
		ModelBlockRenderer modelBlockRenderer = blockRenderDispatcher.getModelRenderer();
		BakedModel model = blockRenderDispatcher.getBlockModel(blockState);
//		blockRenderDispatcher.getModelRenderer().tesselateBlock(level, model, blockState, blockPos, poseStack, vertexConsumer,
//				false, level.getRandom(), blockState.getSeed(blockPos), OverlayTexture.NO_OVERLAY);

		RandomSource random = level.random;
		long seed = blockState.getSeed(blockPos);
		BitSet bitSet = new BitSet(3);

		for (Direction direction : Direction.values()) {
			random.setSeed(seed);
			List<BakedQuad> list = model.getQuads(blockState, direction, random);
			if (!list.isEmpty()) {
				int i = LevelRenderer.getLightColor(level, blockState, blockPos);
				modelBlockRenderer.renderModelFaceFlat(level, blockState, blockPos, i, OverlayTexture.NO_OVERLAY, false,
						poseStack, vertexConsumer, list, bitSet);
			}
		}

		random.setSeed(seed);
		List<BakedQuad> list2 = model.getQuads(blockState, null, random);
		if (!list2.isEmpty()) {
			modelBlockRenderer.renderModelFaceFlat(level, blockState, blockPos, -1, OverlayTexture.NO_OVERLAY, true,
					poseStack, vertexConsumer, list2, bitSet);
		}
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

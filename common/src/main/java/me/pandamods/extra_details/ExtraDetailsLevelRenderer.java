package me.pandamods.extra_details;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import me.pandamods.extra_details.api.extensions.SectionCompilerResultsExtension;
import me.pandamods.extra_details.api.extensions.CompiledSectionExtension;
import me.pandamods.extra_details.api.render.BlockRenderer;
import me.pandamods.extra_details.api.render.BlockRendererRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;

import java.util.SortedSet;

public class ExtraDetailsLevelRenderer {
	private final Minecraft minecraft = Minecraft.getInstance();
	private ClientLevel level = null;
	private LevelRenderer levelRenderer;

	public void prepareRender(LevelRenderer levelRenderer) {
		this.levelRenderer = levelRenderer;
	}

	public void setLevel(ClientLevel level) {
		this.level = level;
	}

	public void renderClientBlockEntities(PoseStack poseStack, float partialTick, double camX, double camY, double camZ,
										  CompiledSectionExtension compiledChunk,
										  RenderBuffers renderBuffers, MultiBufferSource bufferSource,
										  Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress) {
		for (BlockPos blockPos : compiledChunk.getRenderableBlocks()) {
			BlockRenderer renderer = BlockRendererRegistry.get(this.level.getBlockState(blockPos));
			if (renderer == null) continue;

			poseStack.pushPose();
			poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);

			SortedSet<BlockDestructionProgress> breakingInfo = destructionProgress.get(blockPos.asLong());
			if (breakingInfo != null && !breakingInfo.isEmpty()) {
				int stage = breakingInfo.last().getProgress();

				if (stage >= 0) {
					VertexConsumer bufferBuilder = renderBuffers.crumblingBufferSource()
							.getBuffer(ModelBakery.DESTROY_TYPES.get(stage));

					PoseStack.Pose pose = poseStack.last();
					VertexConsumer crumblingConsumer = new SheetedDecalTextureGenerator(bufferBuilder, pose, 1);

					bufferSource = (renderType) -> renderType.affectsCrumbling() ? VertexMultiConsumer
							.create(crumblingConsumer, renderBuffers.bufferSource().getBuffer(renderType)) :
							renderBuffers.bufferSource().getBuffer(renderType);
				}
			}

			renderer.render(blockPos, this.level, poseStack, bufferSource, partialTick, getLightColor(level, blockPos));
			poseStack.popPose();
		}
	}

	private int getLightColor(Level level, BlockPos blockPos) {
		if (level != null) {
			return LevelRenderer.getLightColor(level, blockPos);
		} else {
			return  0xF000F0;
		}
	}

	public void compileBlock(SectionCompilerResultsExtension chunk, BlockGetter blockGetter, BlockPos blockPos) {
		blockPos = blockPos.immutable();
		BlockState blockState = blockGetter.getBlockState(blockPos);

		BlockRenderer renderer = BlockRendererRegistry.get(blockState);
		if (renderer != null) {
			chunk.getRenderableBlocks().add(blockPos);
		}
	}
}

package me.pandamods.extra_details;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntityRegistry;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntityType;
import me.pandamods.extra_details.api.clientblockentity.renderer.ClientBlockEntityRenderDispatcher;
import me.pandamods.extra_details.api.clientblockentity.renderer.ClientBlockEntityRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.List;
import java.util.SortedSet;

public class ExtraDetailsLevelRenderer {
	private final Minecraft minecraft = Minecraft.getInstance();
	private final ClientBlockEntityRenderDispatcher clientBlockEntityRenderDispatcher;
	private ClientLevel level = null;
	private LevelRenderer levelRenderer;

	public ExtraDetailsLevelRenderer(ClientBlockEntityRenderDispatcher clientBlockEntityRenderDispatcher) {
		this.clientBlockEntityRenderDispatcher = clientBlockEntityRenderDispatcher;
	}

	public void prepareRender(LevelRenderer levelRenderer, PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline,
							  Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix) {
		this.levelRenderer = levelRenderer;
		this.clientBlockEntityRenderDispatcher.prepare(this.level, camera, this.minecraft.hitResult);
	}

	public void setLevel(ClientLevel level) {
		this.level = level;
	}

	public void renderClientBlockEntities(PoseStack poseStack, float partialTick, double camX, double camY, double camZ,
										  ObjectArrayList<LevelRenderer.RenderChunkInfo> renderChunksInFrustum,
										  RenderBuffers renderBuffers, MultiBufferSource bufferSource,
										  Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress) {
		for (LevelRenderer.RenderChunkInfo renderChunkInfo : renderChunksInFrustum) {
			List<ClientBlockEntity> blockEntities = renderChunkInfo.chunk.getCompiledChunk().getClientBlockEntities();
			if (blockEntities.isEmpty()) continue;

			for (ClientBlockEntity blockEntity : blockEntities) {
				BlockPos blockPos = blockEntity.getBlockPos();

				poseStack.pushPose();
				poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);

				SortedSet<BlockDestructionProgress> breakingInfo = destructionProgress.get(blockPos.asLong());
				if (breakingInfo != null && !breakingInfo.isEmpty()) {
					int stage = breakingInfo.last().getProgress();

					if (stage >= 0) {
						VertexConsumer bufferBuilder = renderBuffers.crumblingBufferSource()
								.getBuffer(ModelBakery.DESTROY_TYPES.get(stage));

						PoseStack.Pose pose = poseStack.last();
						VertexConsumer crumblingConsumer = new SheetedDecalTextureGenerator(bufferBuilder, pose.pose(), pose.normal(), 1);

						bufferSource = (renderType) -> renderType.affectsCrumbling() ? VertexMultiConsumer
								.create(crumblingConsumer, renderBuffers.bufferSource().getBuffer(renderType)) :
								renderBuffers.bufferSource().getBuffer(renderType);
					}
				}

				clientBlockEntityRenderDispatcher.render(blockEntity, partialTick, poseStack, bufferSource);
				poseStack.popPose();
			}
		}
	}

	public void compileChunk(ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults compileResults, RenderChunkRegion renderChunkRegion, BlockPos startPos, BlockPos endPos) {
		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) return;
		if (renderChunkRegion != null) {
			for (BlockPos pos : BlockPos.betweenClosed(startPos, endPos)) {
				BlockPos blockPos = pos.immutable();
				BlockState blockState = renderChunkRegion.getBlockState(blockPos);

				ClientBlockEntityType<?> blockEntityType = ClientBlockEntityRegistry.get(blockState);
				ClientBlockEntity blockEntity = renderChunkRegion.getClientBlockEntity(blockPos);
				if (blockEntityType != null && (blockEntity == null || !blockEntity.getType().isValid(blockState))) {
					blockEntity = blockEntityType.create(blockPos, blockState);
					level.setClientBlockEntity(blockEntity);
				} else if (blockEntityType == null && blockEntity != null) {
					blockEntity = null;
					level.removeClientBlockEntity(blockPos);
				}

				if (blockEntity != null) {
					blockEntity.setBlockState(blockState);
					this.handleClientBlockEntity(compileResults, blockEntity);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <E extends ClientBlockEntity> void handleClientBlockEntity(ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults compileResults,
																	   ClientBlockEntity blockEntity) {
		ClientBlockEntityRenderer<E> blockEntityRenderer = (ClientBlockEntityRenderer<E>)
				ExtraDetails.blockRenderDispatcher.getRenderer(blockEntity);
		if (blockEntityRenderer != null) {
			compileResults.getClientBlockEntities().add(blockEntity);
		}
	}
}

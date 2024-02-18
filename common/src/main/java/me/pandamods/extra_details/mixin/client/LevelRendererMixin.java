package me.pandamods.extra_details.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.api.client.render.block.BlockRendererDispatcher;
import me.pandamods.extra_details.api.impl.CompileResultsExtension;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;
import java.util.SortedSet;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Shadow @Final private RenderBuffers renderBuffers;

	@Shadow @Nullable private ClientLevel level;

	@Shadow @Final private ObjectArrayList<LevelRenderer.RenderChunkInfo> renderChunksInFrustum;

	@Shadow @Final private Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress;

	private final BlockRendererDispatcher blockRendererDispatcher = ExtraDetails.BLOCK_RENDERER_DISPATCHER;

	@Inject(
			method = "renderLevel",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/renderer/LevelRenderer;globalBlockEntities:Ljava/util/Set;",
					shift = At.Shift.BEFORE,
					ordinal = 0
			)
	)
	public void renderLevel(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera,
							GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
		Vec3 cameraPosition = camera.getPosition();
		double camX = cameraPosition.x;
		double camY = cameraPosition.y;
		double camZ = cameraPosition.z;

		if (this.level != null && !this.renderChunksInFrustum.isEmpty()) {
			for (LevelRenderer.RenderChunkInfo renderChunkInfo : this.renderChunksInFrustum) {
				Set<BlockPos> blocks = ((CompileResultsExtension) renderChunkInfo.chunk.getCompiledChunk()).getRenderableBlocks();
				if (blocks.isEmpty()) continue;

				for (BlockPos blockPos : blocks) {
					poseStack.pushPose();
					poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);
					MultiBufferSource bufferSource = this.renderBuffers.bufferSource();

					int j;
					SortedSet<BlockDestructionProgress> sortedSet = this.destructionProgress.get(blockPos.asLong());
					if (sortedSet != null && !sortedSet.isEmpty() && (j = sortedSet.last().getProgress()) >= 0) {
						PoseStack.Pose pose = poseStack.last();
						SheetedDecalTextureGenerator vertexConsumer = new SheetedDecalTextureGenerator(
								this.renderBuffers.crumblingBufferSource()
										.getBuffer(ModelBakery.DESTROY_TYPES.get(j)), pose.pose(), pose.normal(), 1.0f);
						bufferSource = renderType -> {
							VertexConsumer vertexConsumer2 = this.renderBuffers.bufferSource().getBuffer(renderType);
							if (renderType.affectsCrumbling()) {
								return VertexMultiConsumer.create(vertexConsumer, vertexConsumer2);
							}
							return vertexConsumer2;
						};
					}

					blockRendererDispatcher.render(blockPos, level, poseStack, bufferSource, partialTick);
					poseStack.popPose();
				}
			}
		}
	}
}

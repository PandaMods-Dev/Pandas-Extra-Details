package me.pandamods.extra_details.mixin.pandalib.client;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.pandamods.pandalib.client.render.block.ClientBlockRenderDispatcher;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.impl.CompileResultsExtension;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.*;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Shadow @Final private RenderBuffers renderBuffers;

	@Shadow @Nullable private ClientLevel level;

	@Shadow @Final private ObjectArrayList<LevelRenderer.RenderChunkInfo> renderChunksInFrustum;

	@Shadow protected abstract void checkPoseStack(PoseStack poseStack);

	@Shadow public abstract void clear();

	@Inject(
			method = "renderLevel",
			at = @At(
					value = "INVOKE",
					target = "Lit/unimi/dsi/fastutil/longs/Long2ObjectMap;long2ObjectEntrySet()Lit/unimi/dsi/fastutil/objects/ObjectSet;",
					shift = At.Shift.BY, by = -2
			), locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void renderLevel(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera,
							GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
		ProfilerFiller profilerfiller = this.level.getProfiler();
		MultiBufferSource.BufferSource buffersource = this.renderBuffers.bufferSource();
		Vec3 cameraPosition = camera.getPosition();

		profilerfiller.popPush("clientblocks");
		if (this.level != null && !this.renderChunksInFrustum.isEmpty()) {
			for (LevelRenderer.RenderChunkInfo renderChunkInfo : this.renderChunksInFrustum) {
				Set<BlockPos> clientBlocks = ((CompileResultsExtension) renderChunkInfo.chunk.getCompiledChunk()).getBlocks();
				if (clientBlocks.isEmpty()) continue;
				for (BlockPos blockPos : clientBlocks) {
					ClientBlock clientBlock = ClientBlockRenderDispatcher.CLIENT_BLOCKS.get(blockPos);
					if (clientBlock == null) continue;

					poseStack.pushPose();
					poseStack.translate(
							blockPos.getX() - cameraPosition.x,
							blockPos.getY() - cameraPosition.y,
							blockPos.getZ() - cameraPosition.z
					);
					ClientBlockRenderDispatcher.render(poseStack, buffersource, clientBlock, partialTick);
					poseStack.popPose();
				}
			}
		}

		this.checkPoseStack(poseStack);
		buffersource.endBatch(RenderType.solid());
		buffersource.endBatch(RenderType.entitySolid(TextureAtlas.LOCATION_BLOCKS));
		buffersource.endBatch(RenderType.entityCutout(TextureAtlas.LOCATION_BLOCKS));
		buffersource.endBatch(RenderType.entityCutoutNoCull(TextureAtlas.LOCATION_BLOCKS));
		buffersource.endBatch(RenderType.entitySmoothCutout(TextureAtlas.LOCATION_BLOCKS));
		this.renderBuffers.outlineBufferSource().endOutlineBatch();
	}
}

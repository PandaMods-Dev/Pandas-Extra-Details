package me.pandamods.extra_details.mixin.pandalib.client;

import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.client.render.block.BlockRendererDispatcher;
import me.pandamods.pandalib.mixin_extensions.CompileResultsExtension;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Shadow @Final private RenderBuffers renderBuffers;

	@Shadow @Nullable private ClientLevel level;

	@Shadow @Final private ObjectArrayList<LevelRenderer.RenderChunkInfo> renderChunksInFrustum;

	@Inject(
			method = "renderLevel",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/renderer/LevelRenderer;renderChunksInFrustum:Lit/unimi/dsi/fastutil/objects/ObjectArrayList;",
					shift = At.Shift.BEFORE
			), locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void renderLevel(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera,
							GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
		Vec3 vec3 = camera.getPosition();

		if (this.level != null) {
			for (LevelRenderer.RenderChunkInfo renderChunkInfo : this.renderChunksInFrustum) {
				for (ClientBlock block : ((CompileResultsExtension) renderChunkInfo.chunk.getCompiledChunk()).getBlocks()) {
					BlockState state = block.getBlockState();
					BlockPos pos = block.getBlockPos();

					MultiBufferSource buffer = this.renderBuffers.bufferSource();
					poseStack.pushPose();
					poseStack.translate(pos.getX() - vec3.x, pos.getY() - vec3.y, pos.getZ() - vec3.z);
					BlockRendererDispatcher.render(this.level, poseStack, buffer, block,
							LevelRenderer.getLightColor(this.level, state, pos), OverlayTexture.NO_OVERLAY, partialTick);
					poseStack.popPose();
				}
			}
		}
	}
}

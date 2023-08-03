package me.pandamods.extra_details.mixin.pandalib;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexBuffer;
import me.pandamods.pandalib.mixin_extensions.LevelRendererExtension;
import me.pandamods.pandalib.client.render.block.BlockRenderer;
import me.pandamods.pandalib.client.render.block.BlockRendererDispatcher;
import me.pandamods.pandalib.client.render.block.BlockRendererRegistry;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin implements LevelRendererExtension {
	@Shadow @Final private RenderBuffers renderBuffers;

	@Shadow @Nullable private ClientLevel level;

	@Shadow @Nullable private VertexBuffer starBuffer;

	@Inject(
			method = "renderLevel",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/renderer/LevelRenderer;globalBlockEntities:Ljava/util/Set;",
					shift = At.Shift.BEFORE
			)
	)
	public void renderLevel(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera,
							GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
		this.getBlockRenderers().forEach(pos -> {
			BlockState state = this.level.getBlockState(pos);
			BlockRenderer renderers = BlockRendererRegistry.getFirst(state.getBlock());
			if (renderers != null) {
				MultiBufferSource buffer = this.renderBuffers.bufferSource();
				poseStack.pushPose();
				poseStack.translate(pos.getX(), pos.getY(), pos.getZ());
				BlockRendererDispatcher.render(this.level, poseStack, buffer, camera, renderers, pos,
						LevelRenderer.getLightColor(this.level, state, pos), OverlayTexture.NO_OVERLAY, partialTick);
				poseStack.popPose();
			}
		});
	}
}

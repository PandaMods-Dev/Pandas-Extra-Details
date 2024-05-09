package me.pandamods.extra_details.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.ExtraDetailsLevelRenderer;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.clientblockentity.renderer.ClientBlockEntityRenderDispatcher;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.SortedSet;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Shadow @Final private RenderBuffers renderBuffers;

	@Shadow @Final private ObjectArrayList<LevelRenderer.RenderChunkInfo> renderChunksInFrustum;

	@Shadow @Final private Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress;

	private final ExtraDetailsLevelRenderer edLevelRenderer = ExtraDetails.levelRenderer;

	@Inject(
			method = "renderLevel",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;prepare(Lnet/minecraft/world/level/Level;Lnet/minecraft/client/Camera;Lnet/minecraft/world/phys/HitResult;)V"
			)
	)
	public void prepareRenderLevel(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera,
								   GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
		edLevelRenderer.prepareRender((LevelRenderer) (Object) this, poseStack, partialTick, finishNanoTime, renderBlockOutline,
				camera, gameRenderer, lightTexture, projectionMatrix);
	}

	@Inject(method = "setLevel", at = @At(value = "RETURN"))
	public void setLevel(ClientLevel level, CallbackInfo ci) {
		edLevelRenderer.setLevel(level);
	}

	@Inject(
			method = "renderLevel",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/renderer/LevelRenderer;globalBlockEntities:Ljava/util/Set;",
					shift = At.Shift.BEFORE
			)
	)
	public void renderLevel(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera,
							GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci,
							@Local(ordinal = 0) double camX, @Local(ordinal = 1) double camY, @Local(ordinal = 2) double camZ,
							@Local MultiBufferSource.BufferSource bufferSource) {
		edLevelRenderer.renderClientBlockEntities(poseStack, partialTick, camX, camY, camZ, this.renderChunksInFrustum,
				this.renderBuffers, bufferSource, this.destructionProgress);
	}
}

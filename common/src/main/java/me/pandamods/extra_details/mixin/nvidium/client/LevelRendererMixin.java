package me.pandamods.extra_details.mixin.nvidium.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import me.cortex.nvidium.NvidiumWorldRenderer;
import me.cortex.nvidium.sodiumCompat.INvidiumWorldRendererGetter;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderList;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.util.iterator.ByteIterator;
import me.jellysquid.mods.sodium.client.world.WorldRendererExtended;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.ExtraDetailsLevelRenderer;
import me.pandamods.extra_details.api.extensions.CompiledChunkExtension;
import me.pandamods.extra_details.mixin.sodium.client.SodiumWorldRendererAccessor;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.server.level.BlockDestructionProgress;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.SortedSet;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	@Shadow @Final private RenderBuffers renderBuffers;
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
		SodiumWorldRenderer sodiumWorld = ((WorldRendererExtended) this).sodium$getWorldRenderer();
		NvidiumWorldRenderer nvidiumWorld = ((INvidiumWorldRendererGetter) sodiumWorld).getRenderer();
		for (RenderSection renderSection : nvidiumWorld.getSectionsWithEntities()) {
			edLevelRenderer.renderClientBlockEntities(poseStack, partialTick, camX, camY, camZ, (CompiledChunkExtension) renderSection,
					this.renderBuffers, bufferSource, this.destructionProgress);
		}
	}
}

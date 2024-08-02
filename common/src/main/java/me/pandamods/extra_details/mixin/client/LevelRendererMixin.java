/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.extra_details.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.ExtraDetailsLevelRenderer;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.server.level.BlockDestructionProgress;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.SortedSet;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Shadow @Final private RenderBuffers renderBuffers;

	@Shadow @Final private ObjectArrayList<SectionRenderDispatcher.RenderSection> visibleSections;

	@Shadow @Final private Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress;

	private final ExtraDetailsLevelRenderer edLevelRenderer = ExtraDetails.LEVEL_RENDERER;

	@Inject(
			method = "renderLevel",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;prepare(Lnet/minecraft/world/level/Level;Lnet/minecraft/client/Camera;Lnet/minecraft/world/phys/HitResult;)V"
			)
	)
	public void prepareRenderLevel(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
								   LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci) {
		edLevelRenderer.prepareRender((LevelRenderer) (Object) this);
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
	public void renderLevel(DeltaTracker deltaTracker, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
							LightTexture lightTexture, Matrix4f frustumMatrix, Matrix4f projectionMatrix, CallbackInfo ci,
							@Local(ordinal = 0) double camX, @Local(ordinal = 1) double camY, @Local(ordinal = 2) double camZ,
							@Local MultiBufferSource.BufferSource bufferSource, @Local PoseStack poseStack, @Local(ordinal = 0) float partialTick) {
		for (SectionRenderDispatcher.RenderSection section : this.visibleSections) {
			edLevelRenderer.renderClientBlockEntities(poseStack, partialTick, camX, camY, camZ,
					section.getCompiled(), this.renderBuffers, bufferSource, this.destructionProgress);
		}
	}
}

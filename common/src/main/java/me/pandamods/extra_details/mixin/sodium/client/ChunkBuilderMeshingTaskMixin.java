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

package me.pandamods.extra_details.mixin.sodium.client;

import com.llamalad7.mixinextras.sugar.Local;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;
import me.jellysquid.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderMeshingTask;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.jellysquid.mods.sodium.client.util.task.CancellationToken;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.ExtraDetailsLevelRenderer;
import me.pandamods.extra_details.api.extensions.SectionCompilerResultsExtension;
import me.pandamods.extra_details.api.render.BlockRenderer;
import me.pandamods.extra_details.api.render.BlockRendererRegistry;
import me.pandamods.extra_details.utils.EDSodiumPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = ChunkBuilderMeshingTask.class, remap = false)
public class ChunkBuilderMeshingTaskMixin {
	@Shadow @Final private RenderSection render;
	private final ExtraDetailsLevelRenderer edLevelRenderer = ExtraDetails.LEVEL_RENDERER;

	@Inject(
			method = "execute(Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationToken;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;hasBlockEntity()Z",
					shift = At.Shift.BEFORE,
					ordinal = 1
			)
	)
	public void execute(ChunkBuildContext buildContext, CancellationToken cancellationToken, CallbackInfoReturnable<ChunkBuildOutput> cir,
					 @Local WorldSlice slice, @Local BuiltSectionInfo.Builder renderData, @Local(ordinal = 0) BlockPos.MutableBlockPos blockPos) {
		edLevelRenderer.compileBlock((SectionCompilerResultsExtension) renderData, slice, blockPos);
	}

	@Redirect(
			method = "execute(Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationToken;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;getRenderShape()Lnet/minecraft/world/level/block/RenderShape;"
			)
	)
	public RenderShape compileBlock(BlockState instance, @Local WorldSlice slice, @Local(ordinal = 0) BlockPos.MutableBlockPos blockPos) {
		BlockRenderer renderer = BlockRendererRegistry.get(EDSodiumPlatform.getBlockState(slice, blockPos));
		if (renderer != null) {
			return renderer.getRenderShape();
		}
		return instance.getRenderShape();
	}
}
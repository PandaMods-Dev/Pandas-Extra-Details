package me.pandamods.extra_details.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexSorting;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.ExtraDetailsLevelRenderer;
import me.pandamods.extra_details.api.render.BlockRenderer;
import me.pandamods.extra_details.api.render.BlockRendererRegistry;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;

@Mixin(SectionCompiler.class)
public class SectionCompilerMixin {
	private final ExtraDetailsLevelRenderer edLevelRenderer = ExtraDetails.LEVEL_RENDERER;

	@Inject(method = "compile",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;hasBlockEntity()Z",
					shift = At.Shift.BEFORE
			))
	public void compile(SectionPos sectionPos, RenderChunkRegion region, VertexSorting vertexSorting, SectionBufferBuilderPack sectionBufferBuilderPack,
						CallbackInfoReturnable<SectionCompiler.Results> cir,
						@Local SectionCompiler.Results results,
						@Local(ordinal = 2) BlockPos blockPos, @Local RenderChunkRegion renderChunkRegion) {
		if (renderChunkRegion.getBlockState(blockPos).isAir()) return;
		edLevelRenderer.compileBlock(results, renderChunkRegion, blockPos);
	}

	@Redirect(method = "compile", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;getRenderShape()Lnet/minecraft/world/level/block/RenderShape;"
	))
	public RenderShape compileBlock(BlockState instance, @Local(ordinal = 2) BlockPos blockPos,
									@Local RenderChunkRegion renderChunkRegion) {
		BlockRenderer renderer = BlockRendererRegistry.get(renderChunkRegion.getBlockState(blockPos));
		if (renderer != null) {
			return renderer.getRenderShape();
		}
		return instance.getRenderShape();
	}
}

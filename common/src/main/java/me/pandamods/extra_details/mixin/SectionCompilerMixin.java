package me.pandamods.extra_details.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexSorting;
import me.pandamods.extra_details.api.render.BlockRenderer;
import me.pandamods.extra_details.api.render.ExtraDetailsBlockRenderDispatcher;
import me.pandamods.extra_details.client.ExtraDetailsClient;
import me.pandamods.extra_details.extensions.SectionCompilerResultsExtension;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SectionCompiler.class)
public class SectionCompilerMixin {
	@Inject(method = "compile", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;hasBlockEntity()Z"))
	public void compile(SectionPos sectionPos, RenderChunkRegion region, VertexSorting vertexSorting, SectionBufferBuilderPack sectionBufferBuilderPack,
						CallbackInfoReturnable<SectionCompiler.Results> cir, @Local(name = "blockPos3") BlockPos blockPos, @Local BlockState blockState,
						@Local SectionCompiler.Results results) {
		ExtraDetailsBlockRenderDispatcher renderDispatcher = ExtraDetailsClient.getInstance().blockRenderDispatcher;
		BlockRenderer renderer = renderDispatcher.getRenderer(blockState.getBlock());
		SectionCompilerResultsExtension resultsExtension = (SectionCompilerResultsExtension) (Object) results;
		assert resultsExtension != null;
		
		if (renderer != null) {
			resultsExtension.extraDetails$getRenderableBlocks().add(blockPos.mutable());
		}
	}
}

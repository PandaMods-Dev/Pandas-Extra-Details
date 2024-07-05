package me.pandamods.extra_details.mixin.client;

import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;

@Mixin(SectionRenderDispatcher.RenderSection.RebuildTask.class)
public class RenderSectionRebuildTaskMixin {
	@Inject(method = "doTask",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$CompiledSection;renderableBlockEntities:Ljava/util/List;",
					shift = At.Shift.AFTER
			), locals = LocalCapture.CAPTURE_FAILHARD)
	public void doTask(SectionBufferBuilderPack sectionBufferBuilderPack,
					   CallbackInfoReturnable<CompletableFuture<SectionRenderDispatcher.SectionTaskResult>> cir,
					   RenderChunkRegion renderChunkRegion, SectionPos sectionPos, SectionCompiler.Results results,
					   SectionRenderDispatcher.CompiledSection compiledSection) {
		compiledSection.getRenderableBlocks().addAll(results.getRenderableBlocks());
	}
}

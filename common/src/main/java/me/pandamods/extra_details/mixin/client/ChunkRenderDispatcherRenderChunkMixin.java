package me.pandamods.extra_details.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.ExtraDetailsLevelRenderer;
import me.pandamods.extra_details.api.render.BlockRenderer;
import me.pandamods.extra_details.api.render.BlockRendererRegistry;
import net.minecraft.client.renderer.ChunkBufferBuilderPack;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;

@Mixin(ChunkRenderDispatcher.RenderChunk.RebuildTask.class)
public class ChunkRenderDispatcherRenderChunkMixin {
	@Shadow @Final private ChunkRenderDispatcher.RenderChunk field_20839;
	private final ExtraDetailsLevelRenderer edLevelRenderer = ExtraDetails.LEVEL_RENDERER;

	@Inject(method = "compile",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;hasBlockEntity()Z",
					shift = At.Shift.BEFORE
			))
	public void compile(float x, float y, float z, ChunkBufferBuilderPack chunkBufferBuilderPack,
						CallbackInfoReturnable<ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults> cir,
						@Local ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults compileResults,
						@Local(ordinal = 2) BlockPos blockPos,
						@Local RenderChunkRegion renderChunkRegion) {
		if (renderChunkRegion.getBlockState(blockPos).isAir()) return;
		edLevelRenderer.compileBlock(compileResults, renderChunkRegion, blockPos);
	}

	@Inject(method = "doTask",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$CompiledChunk;renderableBlockEntities:Ljava/util/List;",
					shift = At.Shift.AFTER
			), locals = LocalCapture.CAPTURE_FAILHARD)
	public void doTask(ChunkBufferBuilderPack buffers, CallbackInfoReturnable<CompletableFuture<ChunkRenderDispatcher.ChunkTaskResult>> cir,
					   @Local ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults compileResults,
					   @Local ChunkRenderDispatcher.CompiledChunk compiledChunk) {
		compiledChunk.getRenderableBlocks().addAll(compileResults.getRenderableBlocks());
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

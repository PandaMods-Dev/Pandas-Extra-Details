package me.pandamods.extra_details.mixin.pandalib.client;

import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.client.render.block.ClientBlockProvider;
import me.pandamods.pandalib.client.render.block.ClientBlockRegistry;
import me.pandamods.pandalib.impl.CompileResultsExtension;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ChunkBufferBuilderPack;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mixin(ChunkRenderDispatcher.RenderChunk.RebuildTask.class)
public class ChunkRenderDispatcherRenderChunkMixin {
	@Shadow @Nullable
	protected RenderChunkRegion region;

	@Shadow @Final private ChunkRenderDispatcher.RenderChunk field_20839;
	@Unique
	private RenderChunkRegion captureRegion;

	@Inject(method = "compile", at = @At("HEAD"))
	public void compile(float x, float y, float z, ChunkBufferBuilderPack chunkBufferBuilderPack,
						CallbackInfoReturnable<ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults> cir) {
		this.captureRegion = this.region;
	}

	@Inject(method = "compile",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/vertex/PoseStack;<init>()V",
					shift = At.Shift.BEFORE
			), locals = LocalCapture.CAPTURE_FAILHARD)
	public void compile(float x, float y, float z, ChunkBufferBuilderPack chunkBufferBuilderPack,
						CallbackInfoReturnable<ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults> cir,
						ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults compileResults, int i, BlockPos blockPos, BlockPos blockPos2,
						VisGraph visGraph, RenderChunkRegion renderChunkRegion) {
		if (this.captureRegion != null) {
			for (BlockPos pos : BlockPos.betweenClosed(blockPos, blockPos2)) {
				BlockState state = this.captureRegion.getBlockState(pos);
				if (state.isAir())
					continue;
				ClientBlockProvider blockProvider = ClientBlockRegistry.get(state.getBlock());
				if (blockProvider != null) {
					List<ClientBlock> compiledBlocks = ((CompileResultsExtension) this.field_20839.getCompiledChunk()).getBlocks().stream().filter(
							clientBlock -> clientBlock.getBlockPos().equals(pos.immutable()) && clientBlock.getBlockState().is(state.getBlock())).toList();
					if (compiledBlocks.isEmpty()) {
						((CompileResultsExtension) (Object) compileResults).getBlocks().add(
							blockProvider.create(pos.immutable(), state, Minecraft.getInstance().level));
					} else {
						ClientBlock block = compiledBlocks.get(0);
						block.setBlockState(state);
						((CompileResultsExtension) (Object) compileResults).getBlocks().add(block);
					}
				}
			}
		}
	}

	@Inject(method = "doTask",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$CompiledChunk;renderableBlockEntities:Ljava/util/List;",
					shift = At.Shift.AFTER
			), locals = LocalCapture.CAPTURE_FAILHARD)
	public void doTask(ChunkBufferBuilderPack buffers, CallbackInfoReturnable<CompletableFuture<ChunkRenderDispatcher.ChunkTaskResult>> cir,
					   Vec3 vec3, float f, float g, float h, ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults compileResults,
					   ChunkRenderDispatcher.CompiledChunk compiledChunk) {
		((CompileResultsExtension) compiledChunk).getBlocks().addAll(((CompileResultsExtension) (Object) compileResults).getBlocks());
	}
}

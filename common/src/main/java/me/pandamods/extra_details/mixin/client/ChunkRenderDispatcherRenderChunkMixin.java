package me.pandamods.extra_details.mixin.client;

import me.pandamods.extra_details.api.client.render.block.ClientBlockRenderDispatcher;
import me.pandamods.extra_details.api.impl.CompileResultsExtension;
import me.pandamods.extra_details.api.utils.ClientBlockUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ChunkBufferBuilderPack;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

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
						ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults compileResults, int i, BlockPos startPos, BlockPos endPos,
						VisGraph visGraph, RenderChunkRegion renderChunkRegion) {
		ClientLevel level = Minecraft.getInstance().level;
		if (this.captureRegion != null) {
			ClientBlockUtils.compileChunk(this.captureRegion, level, startPos, endPos, this.field_20839.getCompiledChunk(), compileResults);
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

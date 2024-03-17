package me.pandamods.extra_details.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.ExtraDetailsLevelRenderer;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntityRegistry;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntityType;
import me.pandamods.extra_details.api.clientblockentity.renderer.ClientBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.ChunkBufferBuilderPack;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;

@Mixin(ChunkRenderDispatcher.RenderChunk.RebuildTask.class)
public class ChunkRenderDispatcherRenderChunkMixin {
	private final ExtraDetailsLevelRenderer edLevelRenderer = ExtraDetails.levelRenderer;

	@Inject(method = "compile",
			at = @At(
					value = "INVOKE",
					target = "Lcom/mojang/blaze3d/vertex/PoseStack;<init>()V",
					shift = At.Shift.BEFORE
			))
	public void compile(float x, float y, float z, ChunkBufferBuilderPack chunkBufferBuilderPack,
						CallbackInfoReturnable<ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults> cir,
						@Local ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults compileResults,
						@Local(name = "blockPos") BlockPos startPos, @Local(name = "blockPos2") BlockPos endPos,
						@Local RenderChunkRegion renderChunkRegion) {
		edLevelRenderer.compileChunk(compileResults, renderChunkRegion, startPos, endPos);
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
		compiledChunk.getClientBlockEntities().addAll(compileResults.getClientBlockEntities());
	}

	@Redirect(method = "compile", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;getRenderShape()Lnet/minecraft/world/level/block/RenderShape;"
	))
	public RenderShape compileBlock(BlockState instance, @Local(name = "blockPos3") BlockPos blockPos,
									@Local(name = "renderChunkRegion") RenderChunkRegion renderChunkRegion) {
		ClientBlockEntityType<?> blockEntityType = ClientBlockEntityRegistry.get(instance);
		if (blockEntityType != null && blockEntityType.shouldHideBase()) {
			return RenderShape.INVISIBLE;
		}
		return instance.getRenderShape();
	}
}

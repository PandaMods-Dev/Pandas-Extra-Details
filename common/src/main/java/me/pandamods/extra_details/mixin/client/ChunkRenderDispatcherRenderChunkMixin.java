package me.pandamods.extra_details.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntityRegistry;
import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntityType;
import me.pandamods.extra_details.api.client.render.block.ClientBlockEntityRenderer;
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
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
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
		if (level == null) return;
		if (renderChunkRegion != null) {
			for (BlockPos pos : BlockPos.betweenClosed(startPos, endPos)) {
				BlockPos blockPos = pos.immutable();
				BlockState blockState = renderChunkRegion.getBlockState(blockPos);

				ClientBlockEntityType<?> blockEntityType = ClientBlockEntityRegistry.get(blockState);
				ClientBlockEntity blockEntity = renderChunkRegion.getClientBlockEntity(blockPos);
				if (blockEntityType != null && (blockEntity == null || !blockEntity.getType().isValid(blockState))) {
					blockEntity = blockEntityType.create(blockPos, blockState);
					level.setClientBlockEntity(blockEntity);
				} else if (blockEntityType == null && blockEntity != null) {
					blockEntity = null;
					level.removeClientBlockEntity(blockPos);
				}

				if (blockEntity != null) {
					blockEntity.setBlockState(blockState);
					this.handleClientBlockEntity(compileResults, blockEntity);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <E extends ClientBlockEntity> void handleClientBlockEntity(ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults compileResults,
																	   ClientBlockEntity blockEntity) {
		ClientBlockEntityRenderer<E> blockEntityRenderer = (ClientBlockEntityRenderer<E>)
				ExtraDetails.blockRenderDispatcher.getRenderer(blockEntity);
		if (blockEntityRenderer != null) {
			compileResults.getClientBlockEntities().add(blockEntity);
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

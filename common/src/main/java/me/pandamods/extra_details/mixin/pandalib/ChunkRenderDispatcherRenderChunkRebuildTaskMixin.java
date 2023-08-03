package me.pandamods.extra_details.mixin.pandalib;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.client.renderer.block.door.DoorRenderer;
import me.pandamods.pandalib.client.render.block.BlockRenderer;
import me.pandamods.pandalib.client.render.block.BlockRendererDispatcher;
import me.pandamods.pandalib.client.render.block.BlockRendererRegistry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ChunkBufferBuilderPack;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Mixin(ChunkRenderDispatcher.RenderChunk.RebuildTask.class)
public class ChunkRenderDispatcherRenderChunkRebuildTaskMixin {
	@Shadow @Nullable
	protected RenderChunkRegion region;

	@Unique
    private RenderChunkRegion captureRegion;

	@Inject(method = "compile", at = @At("HEAD"))
    public void captureRegion(float x, float y, float z, ChunkBufferBuilderPack chunkBufferBuilderPack,
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

//				((CompiledChunkExtension) (Object) compileResults).getBlockRenderPositions().add(pos.immutable());
			}
		}
	}
}

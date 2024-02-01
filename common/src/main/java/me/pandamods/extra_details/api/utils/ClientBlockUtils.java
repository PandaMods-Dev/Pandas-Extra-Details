package me.pandamods.extra_details.api.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.api.client.render.block.*;
import me.pandamods.extra_details.api.impl.CompileResultsExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClientBlockUtils {
	public static void compileChunk(RenderChunkRegion captureRegion, ClientLevel level, BlockPos startPos, BlockPos endPos,
									ChunkRenderDispatcher.CompiledChunk compiledChunk,
									ChunkRenderDispatcher.RenderChunk.RebuildTask.CompileResults compileResults) {
		CompileResultsExtension prevCompileResults = ((CompileResultsExtension) compiledChunk);
		CompileResultsExtension nextCompileResults = ((CompileResultsExtension) (Object) compileResults);

		for (BlockPos pos : BlockPos.betweenClosed(startPos, endPos)) {
			BlockState state = captureRegion.getBlockState(pos.immutable());
			if (state.isAir()) {
				ClientBlockRenderDispatcher.RENDERERS.remove(pos);
				continue;
			}

			ClientBlockUtils.compileBlock(level, state, pos.immutable(), prevCompileResults, nextCompileResults);
		}
	}

	public static void compileBlock(ClientLevel level, BlockState blockState, BlockPos blockPos,
									CompileResultsExtension prevCompileResults, CompileResultsExtension nextCompileResults) {
		Map<BlockPos, BlockRenderer> renderers = ClientBlockRenderDispatcher.RENDERERS;
		BlockRendererProvider provider = BlockRendererRegistry.get(blockState.getBlock());

		if (provider != null) {
			if (renderers.values().stream().noneMatch(blockRenderer -> blockState.is(blockRenderer.getBlock()))) {
				renderers.put(blockPos, provider.create(blockState.getBlock(), level, blockPos));
			}
			nextCompileResults.getBlocks().add(blockPos);
		} else renderers.remove(blockPos);
	}

	public static void render(PoseStack poseStack, CompileResultsExtension compileResults, Vec3 cameraPosition,
							  MultiBufferSource multiBufferSource, float partialTick) {
		Set<BlockPos> blocks = compileResults.getBlocks();
		if (blocks.isEmpty()) return;
		for (BlockPos blockPos : blocks) {
			poseStack.pushPose();
			poseStack.translate(
					blockPos.getX() - cameraPosition.x,
					blockPos.getY() - cameraPosition.y,
					blockPos.getZ() - cameraPosition.z
			);

			BlockRenderer renderer = ClientBlockRenderDispatcher.RENDERERS.get(blockPos);
			if (renderer != null) renderer.render(poseStack, multiBufferSource);
			poseStack.popPose();
		}
	}
}

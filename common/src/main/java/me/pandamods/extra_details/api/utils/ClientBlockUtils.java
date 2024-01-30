package me.pandamods.extra_details.api.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.api.client.render.block.*;
import me.pandamods.extra_details.api.impl.CompileResultsExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ClientBlockUtils {
	public static void compile(Level level, BlockState blockState, BlockPos blockPos,
							   CompileResultsExtension prevCompileResults, CompileResultsExtension nextCompileResults) {
		List<BlockRendererProvider> providers = BlockRendererRegistry.get(blockState);

		ClientBlockRenderDispatcher.RENDERERS.removeIf(blockRenderer -> providers.isEmpty() && blockRenderer.getBlockPos().equals(blockPos));

		if (level == null) return;
		for (BlockRendererProvider provider : providers) {
			Optional<BlockPos> compiledBlockPos = prevCompileResults.getBlocks().stream().filter(
					pos -> pos.equals(blockPos) && !BlockRendererRegistry.get(level.getBlockState(pos)).isEmpty()
			).findFirst();
			if (ClientBlockRenderDispatcher.RENDERERS.stream().noneMatch(blockRenderer -> blockRenderer.getBlockPos().equals(blockPos)) ||
					compiledBlockPos.isEmpty()) {
				ClientBlockRenderDispatcher.RENDERERS.add(provider.create(Minecraft.getInstance().level, blockPos));
				nextCompileResults.getBlocks().add(blockPos);
			} else if (ClientBlockRenderDispatcher.RENDERERS.stream().anyMatch(blockRenderer -> blockRenderer.getBlockPos().equals(blockPos))) {
				nextCompileResults.getBlocks().add(blockPos);
			}
		}
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

			List<BlockRenderer> renderers = ClientBlockRenderDispatcher.RENDERERS.stream()
					.filter(blockRenderer -> blockRenderer.getBlockPos().equals(blockPos)).toList();
			ClientBlockRenderDispatcher.render(renderers, partialTick, poseStack, multiBufferSource);
			poseStack.popPose();
		}
	}
}

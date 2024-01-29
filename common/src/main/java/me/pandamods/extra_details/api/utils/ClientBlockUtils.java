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

import java.util.Optional;
import java.util.Set;

public class ClientBlockUtils {
	public static void compile(Level level, BlockState blockState, BlockPos blockPos,
							   CompileResultsExtension prevCompileResults, CompileResultsExtension nextCompileResults) {
		Optional<ClientBlockType<?>> clientBlockTypeOptional = ClientBlockRegistry.getType(blockState.getBlock());
		if (clientBlockTypeOptional.isEmpty()) {
			return;
		}
		ClientBlockType<?> clientBlockType = clientBlockTypeOptional.get();
		ClientBlockProvider blockProvider = clientBlockType.provider;

		if ((!ClientBlockRenderDispatcher.CLIENT_BLOCKS.containsKey(blockPos) ||
				!clientBlockType.isValid(ClientBlockRenderDispatcher.CLIENT_BLOCKS.get(blockPos).getBlockState()))
				&& blockProvider != null && level != null)
			ClientBlockRenderDispatcher.CLIENT_BLOCKS.remove(blockPos);

		if (blockProvider != null && level != null) {
			Optional<BlockPos> compiledBlockPos = prevCompileResults.getBlocks().stream().filter(
					clientBlockPos -> clientBlockPos.equals(blockPos) && clientBlockType.isValid(level.getBlockState(clientBlockPos))
			).findFirst();
			if (!ClientBlockRenderDispatcher.CLIENT_BLOCKS.containsKey(blockPos) || compiledBlockPos.isEmpty()) {
				ClientBlockRenderDispatcher.CLIENT_BLOCKS.put(blockPos,
						blockProvider.create(clientBlockType, blockPos, Minecraft.getInstance().level));
				nextCompileResults.getBlocks().add(blockPos);
			} else {
				ClientBlock block = ClientBlockRenderDispatcher.CLIENT_BLOCKS.get(blockPos);
				if (block != null) {
					nextCompileResults.getBlocks().add(blockPos);
				}
			}
		}
	}

	public static void render(PoseStack poseStack, CompileResultsExtension compileResults, Vec3 cameraPosition,
							  MultiBufferSource multiBufferSource, float partialTick) {
		Set<BlockPos> clientBlocks = compileResults.getBlocks();
		if (clientBlocks.isEmpty()) return;
		for (BlockPos blockPos : clientBlocks) {
			ClientBlock clientBlock = ClientBlockRenderDispatcher.CLIENT_BLOCKS.get(blockPos);
			if (clientBlock == null) continue;

			poseStack.pushPose();
			poseStack.translate(
					blockPos.getX() - cameraPosition.x,
					blockPos.getY() - cameraPosition.y,
					blockPos.getZ() - cameraPosition.z
			);

			ClientBlockRenderDispatcher.render(clientBlock, partialTick, poseStack, multiBufferSource);
			poseStack.popPose();
		}
	}
}

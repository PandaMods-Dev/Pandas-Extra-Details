package me.pandamods.extra_details.mixin.pandalib.sodium;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.ChunkBuildOutput;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderCache;
import me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import me.jellysquid.mods.sodium.client.render.chunk.compile.tasks.ChunkBuilderMeshingTask;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.jellysquid.mods.sodium.client.util.task.CancellationToken;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.client.render.block.ClientBlockProvider;
import me.pandamods.pandalib.client.render.block.ClientBlockRegistry;
import me.pandamods.pandalib.mixin_extensions.CompileResultsExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(ChunkBuilderMeshingTask.class)
public class ChunkBuilderMeshingTaskMixin {
	@Shadow @Final private RenderSection render;

	@Inject(
			method = "execute(Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildContext;Lme/jellysquid/mods/sodium/client/util/task/CancellationToken;)Lme/jellysquid/mods/sodium/client/render/chunk/compile/ChunkBuildOutput;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/state/BlockState;hasBlockEntity()Z",
					shift = At.Shift.BEFORE
			),
			locals = LocalCapture.CAPTURE_FAILEXCEPTION
	)
	public void execute(ChunkBuildContext buildContext, CancellationToken cancellationToken, CallbackInfoReturnable<ChunkBuildOutput> cir,
						BuiltSectionInfo.Builder renderData, VisGraph occluder, ChunkBuildBuffers buffers, BlockRenderCache cache, WorldSlice slice,
						int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockPos.MutableBlockPos blockPos, BlockPos.MutableBlockPos modelOffset,
						BlockRenderContext context, int y, int z, int x, BlockState blockState, FluidState fluidState) {
		ClientBlockProvider blockProvider = ClientBlockRegistry.get(blockState.getBlock());
		if (blockProvider != null) {
			List<ClientBlock> compiledBlocks = ((CompileResultsExtension) this.render).getBlocks().stream().filter(
							clientBlock -> clientBlock.getBlockPos().equals(blockPos.immutable()) && clientBlock.getBlockState()
									.is(blockState.getBlock())).toList();
			if (compiledBlocks.isEmpty()) {
				((CompileResultsExtension) renderData).getBlocks().add(
						blockProvider.create(blockPos.immutable(), blockState, Minecraft.getInstance().level));
			} else {
				ClientBlock block = compiledBlocks.get(0);
				block.setBlockState(blockState);
				((CompileResultsExtension) renderData).getBlocks().add(block);
			}
		}
	}
}

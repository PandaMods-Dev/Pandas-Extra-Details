package me.pandamods.extra_details.mixin.pandalib.block;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.pandalib.client.render.block.BlockRendererRegistry;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.client.render.block.ClientBlockRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
	@Inject(method = "setupAndRender", at = @At("HEAD"), cancellable = true)
	private static <T extends BlockEntity> void setupAndRender(BlockEntityRenderer<T> renderer, T blockEntity, float partialTick, PoseStack poseStack,
															   MultiBufferSource bufferSource, CallbackInfo ci) {
		ClientBlockRenderer<ClientBlock> clientRenderer = BlockRendererRegistry.get(blockEntity.getBlockState().getBlock());
		if (clientRenderer != null)
			ci.cancel();
	}
}

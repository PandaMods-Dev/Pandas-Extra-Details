package me.pandamods.extra_details.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.extra_details.api.client.render.block.BlockRendererRegistry;
import me.pandamods.extra_details.api.client.render.block.ClientBlock;
import me.pandamods.extra_details.api.client.render.block.ClientBlockRenderer;
import me.pandamods.extra_details.pandalib.client.render.MeshRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {
	@Inject(method = "renderItem", at = @At("HEAD"))
	public <E extends BlockEntity> void renderItemHead(E blockEntity, PoseStack poseStack, MultiBufferSource bufferSource,
													   int packedLight, int packedOverlay, CallbackInfoReturnable<Boolean> cir) {
		poseStack.pushPose();
	}

	@Inject(method = "renderItem", at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;tryRender(Lnet/minecraft/world/level/block/entity/BlockEntity;Ljava/lang/Runnable;)V"
	), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public <E extends BlockEntity> void renderItem(E blockEntity, PoseStack poseStack, MultiBufferSource bufferSource,
												   int packedLight, int packedOverlay, CallbackInfoReturnable<Boolean> cir,
												   BlockEntityRenderer<E> blockEntityRenderer) {
		if (blockEntityRenderer instanceof MeshRenderer<?,?>) {
			poseStack.translate(0.5, 0.5, 0.5);
			poseStack.mulPose(Axis.YP.rotationDegrees(180));
			poseStack.translate(-0.5, -0.5, -0.5);
		}
	}

	@Inject(method = "renderItem", at = @At("RETURN"))
	public <E extends BlockEntity> void renderItemReturn(E blockEntity, PoseStack poseStack, MultiBufferSource bufferSource,
													   int packedLight, int packedOverlay, CallbackInfoReturnable<Boolean> cir) {
		poseStack.popPose();
	}

	@Inject(method = "setupAndRender", at = @At("HEAD"), cancellable = true)
	private static <T extends BlockEntity> void setupAndRender(BlockEntityRenderer<T> renderer, T blockEntity, float partialTick, PoseStack poseStack,
															   MultiBufferSource bufferSource, CallbackInfo ci) {
		ClientBlockRenderer<ClientBlock> clientRenderer = BlockRendererRegistry.get(blockEntity.getBlockState().getBlock());
		if (clientRenderer != null && clientRenderer.enabled(blockEntity.getBlockState()))
			ci.cancel();
	}
}

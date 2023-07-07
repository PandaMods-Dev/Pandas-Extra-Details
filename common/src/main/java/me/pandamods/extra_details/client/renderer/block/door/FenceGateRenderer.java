package me.pandamods.extra_details.client.renderer.block.door;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.client.model.block.FenceGateModel;
import me.pandamods.extra_details.entity.block.FenceGateEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.FenceGateBlock;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FenceGateRenderer extends GeoBlockRenderer<FenceGateEntity> {
	public FenceGateRenderer() {
		super(new FenceGateModel());
	}

	@Override
	public void actuallyRender(PoseStack poseStack, FenceGateEntity animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.pushPose();
		if (animatable.getBlockState().getValue(FenceGateBlock.IN_WALL))
			poseStack.translate(0, (float) (-2) /16, 0);
		super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
		poseStack.popPose();
	}
}

package me.pandamods.extra_details.client.renderer.block.door;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.model.block.FenceGateModel;
import me.pandamods.extra_details.entity.block.FenceGateEntity;
import me.pandamods.extra_details.utils.RenderUtils;
import me.pandamods.pandalib.client.render.MeshBlockRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.FenceGateBlock;

import java.util.List;

public class FenceGateRenderer extends MeshBlockRenderer<FenceGateEntity, FenceGateModel> {
	public FenceGateRenderer() {
		super(new FenceGateModel());
	}

	@Override
	public void render(FenceGateEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		stack.pushPose();
		if (blockEntity.getBlockState().getValue(FenceGateBlock.IN_WALL)) {
			stack.translate(0, (float) -3 /16, 0);
		}
		super.render(blockEntity, partialTick, stack, buffer, packedLight, packedOverlay);
		stack.popPose();
	}
}

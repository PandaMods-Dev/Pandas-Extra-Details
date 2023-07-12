package me.pandamods.extra_details.api.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PandaBlockGeoRenderer<T extends BlockEntity> implements PandaGeoRenderer<T>, BlockEntityRenderer<T> {
	@Override
	public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

	}
}

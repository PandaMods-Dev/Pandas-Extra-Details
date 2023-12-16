package me.pandamods.extra_details.client.renderer.block.chest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.model.block.chest.EnderChestModel;
import me.pandamods.pandalib.client.render.MeshRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public class AnimatedEnderChestRenderer extends ChestRenderer<EnderChestBlockEntity>
		implements MeshRenderer<EnderChestBlockEntity, EnderChestModel> {
	private final EnderChestModel model = new EnderChestModel();

	@Override
	public VertexConsumer getVertexConsumer(MultiBufferSource bufferSource, ResourceLocation location, EnderChestBlockEntity base) {
		BlockState blockState = base.getBlockState();
		ChestType chestType = blockState.hasProperty(ChestBlock.TYPE) ? blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
		return bufferSource.getBuffer(RenderType.entityCutout(
				new ResourceLocation("textures/" + Sheets.chooseMaterial(base, chestType, xmasTextures).texture().getPath() + ".png")));
	}

	public AnimatedEnderChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(EnderChestBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer,
					   int packedLight, int packedOverlay) {
		if (ExtraDetails.getConfig().blockSettings.chest.enabled) {
			poseStack.pushPose();
			translateBlock(blockEntity.getBlockState(), poseStack);
			this.renderRig(blockEntity, model, poseStack, buffer, packedLight, packedOverlay);
			poseStack.popPose();
		} else {
			super.render(blockEntity, partialTick, poseStack, buffer, packedLight, packedOverlay);
		}
	}
}

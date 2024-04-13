package me.pandamods.extra_details.client.renderer.block.chest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.model.block.chest.ChestModel;
import me.pandamods.extra_details.pandalib.client.render.MeshRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public class AnimatedChestRenderer<T extends ChestBlockEntity> extends ChestRenderer<T> implements MeshRenderer<T, ChestModel<T>> {
	private final ChestModel<T> model = new ChestModel<>();

	@Override
	public VertexConsumer getVertexConsumer(MultiBufferSource bufferSource, ResourceLocation location, T base) {
		BlockState blockState = base.getBlockState();
		ChestType chestType = blockState.hasProperty(ChestBlock.TYPE) ? blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
		return bufferSource.getBuffer(RenderType.entityCutout(
				new ResourceLocation("textures/" + Sheets.chooseMaterial(base, chestType, xmasTextures).texture().getPath() + ".png")));
	}

	public AnimatedChestRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
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

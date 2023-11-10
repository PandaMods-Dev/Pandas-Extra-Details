package me.pandamods.extra_details.client.renderer.block.door;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.model.block.door.TrapDoorModel;
import me.pandamods.extra_details.entity.block.TrapDoorClientBlock;
import me.pandamods.pandalib.client.render.block.extensions.MeshClientBlockRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

@Environment(EnvType.CLIENT)
public class TrapDoorRenderer extends MeshClientBlockRenderer<TrapDoorClientBlock, TrapDoorModel> {
	public TrapDoorRenderer() {
		super(new TrapDoorModel());
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return ExtraDetails.getConfig().blockSettings.trapdoor.enabled ? RenderShape.INVISIBLE : RenderShape.MODEL;
	}

	@Override
	public void render(TrapDoorClientBlock block, PoseStack poseStack, MultiBufferSource buffer, int lightColor, int overlay, float partialTick) {
		if (ExtraDetails.getConfig().blockSettings.trapdoor.enabled) {
			poseStack.pushPose();
			if (block.getBlockState().getValue(TrapDoorBlock.HALF).equals(Half.TOP))
				poseStack.translate(0, 13f / 16, 0);
			super.render(block, poseStack, buffer, lightColor, overlay, partialTick);
			poseStack.popPose();
		}
	}
}

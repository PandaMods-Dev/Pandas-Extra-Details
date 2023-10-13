package me.pandamods.extra_details.client.renderer.block.door;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.model.block.TrapDoorModel;
import me.pandamods.extra_details.entity.block.TrapDoorClientBlock;
import me.pandamods.pandalib.client.render.block.extensions.MeshClientBlockRenderer;
import me.pandamods.pandalib.utils.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class TrapDoorRenderer extends MeshClientBlockRenderer<TrapDoorClientBlock, TrapDoorModel> {
	public TrapDoorRenderer() {
		super(new TrapDoorModel());
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return ExtraDetails.getConfig().enable_trap_door_animation ? RenderShape.INVISIBLE : RenderShape.MODEL;
	}

	@Override
	public void render(TrapDoorClientBlock block, PoseStack poseStack, MultiBufferSource buffer, int lightColor, int overlay, float partialTick) {
		if (ExtraDetails.getConfig().enable_trap_door_animation) {
			super.render(block, poseStack, buffer, lightColor, overlay, partialTick);
		}
	}
}

package me.pandamods.extra_details.client.renderer.block.redstone;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.extra_details.client.model.block.LeverModel;
import me.pandamods.extra_details.entity.block.FenceGateClientBlock;
import me.pandamods.extra_details.entity.block.LeverClientBlock;
import me.pandamods.pandalib.client.render.MeshBlockEntityRenderer;
import me.pandamods.pandalib.client.render.block.extensions.MeshClientBlockRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class LeverRenderer extends MeshClientBlockRenderer<LeverClientBlock, LeverModel> {
	public LeverRenderer() {
		super(new LeverModel());
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public void render(LeverClientBlock block, PoseStack poseStack, MultiBufferSource buffer, int lightColor, int overlay, float partialTick) {
		super.render(block, poseStack, buffer, lightColor, overlay, partialTick);
	}
}
package me.pandamods.extra_details.client.renderer.block.redstone;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.extra_details.client.model.block.LeverModel;
import me.pandamods.extra_details.entity.block.LeverEntity;
import me.pandamods.pandalib.client.render.MeshBlockRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class LeverRenderer extends MeshBlockRenderer<LeverEntity, LeverModel> {
	public LeverRenderer() {
		super(new LeverModel());
	}

	@Override
	protected void translateBlock(LeverEntity blockEntity, PoseStack stack) {
		stack.translate(0.5f, 0.5f, 0.5f);

		Direction direction = getFacing(blockEntity);
		stack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));

		AttachFace face = blockEntity.getBlockState().getValue(LeverBlock.FACE);
		switch (face) {
			case CEILING -> stack.mulPose(Axis.XP.rotationDegrees(180));
			case WALL -> stack.mulPose(Axis.XP.rotationDegrees(90));
		}

		stack.translate(0, -0.5f, 0);
	}
}
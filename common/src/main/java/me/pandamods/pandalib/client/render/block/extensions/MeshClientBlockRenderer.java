package me.pandamods.pandalib.client.render.block.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.client.render.MeshRenderer;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.client.render.block.ClientBlockRenderer;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class MeshClientBlockRenderer<T extends ClientBlock & MeshAnimatable, M extends MeshModel<T>>
		implements ClientBlockRenderer<T>, MeshRenderer<T, M> {
	private final M model;

	public MeshClientBlockRenderer(M model) {
		this.model = model;
	}

	@Override
	public void render(T block, PoseStack poseStack, MultiBufferSource buffer, int lightColor, int overlay, float partialTick) {
		poseStack.pushPose();
		translateBlock(block, poseStack);
		this.renderMesh(block, this.model, poseStack, buffer, partialTick, lightColor, overlay);
		poseStack.popPose();
	}

	protected void translateBlock(T block, PoseStack stack) {
		BlockState blockState = block.getBlockState();

		stack.translate(0.5f, 0.5f, 0.5f);

		Direction direction = getFacing(block);
		stack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));

		if (blockState.hasProperty(BlockStateProperties.ATTACH_FACE)) {
			AttachFace face = blockState.getValue(BlockStateProperties.ATTACH_FACE);
			switch (face) {
				case CEILING -> stack.mulPose(Axis.XP.rotationDegrees(180));
				case WALL -> stack.mulPose(Axis.XP.rotationDegrees(90));
			}
		}

		stack.translate(0, -0.5f, 0);
	}

	protected Direction getFacing(T block) {
		BlockState blockState = block.getBlockState();

		if (blockState.hasProperty(HorizontalDirectionalBlock.FACING))
			return blockState.getValue(HorizontalDirectionalBlock.FACING);

		if (blockState.hasProperty(DirectionalBlock.FACING))
			return blockState.getValue(DirectionalBlock.FACING);

		return Direction.NORTH;
	}
}

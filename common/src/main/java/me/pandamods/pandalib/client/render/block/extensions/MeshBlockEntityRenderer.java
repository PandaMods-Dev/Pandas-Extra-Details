package me.pandamods.pandalib.client.render.block.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.client.render.MeshRenderer;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

@Environment(EnvType.CLIENT)
public abstract class MeshBlockEntityRenderer<T extends BlockEntity & MeshAnimatable, M extends MeshModel<T>>
		implements MeshRenderer<T, M>, BlockEntityRenderer<T> {
	private final M model;

	public MeshBlockEntityRenderer(M model) {
		this.model = model;
	}


	@Override
	public void render(T blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		stack.pushPose();
		translateBlock(blockEntity, stack);
		this.renderMesh(blockEntity, model, stack, buffer, packedLight, packedOverlay);
		stack.popPose();
	}

	public void translateBlock(T block, PoseStack stack) {
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

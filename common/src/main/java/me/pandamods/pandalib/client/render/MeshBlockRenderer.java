package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public abstract class MeshBlockRenderer<T extends BlockEntity & MeshAnimatable, M extends MeshModel<T>>
		extends MeshRenderer<T, M> implements BlockEntityRenderer<T> {
	public MeshBlockRenderer(M model) {
		super(model);
	}

	@Override
	public void render(T blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		stack.pushPose();
		translateBlock(blockEntity, stack);
		this.renderMesh(blockEntity, stack, buffer, partialTick, packedLight, packedOverlay);
		stack.popPose();
	}

	protected void translateBlock(T blockEntity, PoseStack stack) {
		stack.translate(0.5f, 0.5f, 0.5f);

		Direction direction = getFacing(blockEntity);
		stack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));

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

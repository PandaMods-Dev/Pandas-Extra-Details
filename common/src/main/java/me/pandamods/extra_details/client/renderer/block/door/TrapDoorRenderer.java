package me.pandamods.extra_details.client.renderer.block.door;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.TrapDoorEntity;
import me.pandamods.extra_details.utils.RenderUtils;
import me.pandamods.extra_details.utils.VectorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import org.joml.Math;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class TrapDoorRenderer implements BlockEntityRenderer<TrapDoorEntity> {
	final BlockRenderDispatcher blockRender;

	public TrapDoorRenderer(BlockEntityRendererProvider.Context context) {
		this.blockRender = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(TrapDoorEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
		BlockState blockState = blockEntity.getBlockState();
		BlockState state = blockEntity.getBlockState().getBlock().defaultBlockState()
				.setValue(TrapDoorBlock.HALF, blockState.getValue(TrapDoorBlock.HALF));

		float speed = (f/15)* ExtraDetails.getConfig().trap_door_animation_speed;

		blockEntity.openingTime = Math.clamp(0, 1, blockEntity.openingTime + (blockState.getValue(DoorBlock.OPEN) ? speed : -speed));

		Vector3f pivot = new Vector3f(0, blockState.getValue(TrapDoorBlock.HALF).equals(Half.TOP) ? 14.5f : 1.5f, 14.5f);

		poseStack.pushPose();
		VectorUtils.rotateByPivot(
				poseStack,
				new Vector3f(8, 0, 8),
				VectorUtils.toRadians(new Vector3f(0, -blockState.getValue(TrapDoorBlock.FACING).toYRot() + 180, 0))
		);

		float openMaxRotation = blockState.getValue(TrapDoorBlock.HALF).equals(Half.TOP) ? -90 : 90;
		float animValue = blockState.getValue(DoorBlock.OPEN) ?
				DoorRenderer.doorAnimation.getValue(blockEntity.openingTime) :
				1 - DoorRenderer.doorAnimation.getValue(1 - blockEntity.openingTime);

		VectorUtils.rotateByPivot(
				poseStack,
				new Vector3f(pivot.x, pivot.y, pivot.z),
				new Vector3f(
						Math.toRadians(animValue * openMaxRotation),
						0,
						0
				)
		);

		RenderUtils.renderBlock(poseStack, state, multiBufferSource, i, j);
		poseStack.popPose();
	}
}

package me.pandamods.extra_details.client.renderer.block.door;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.DoorEntity;
import me.pandamods.extra_details.utils.RenderUtils;
import me.pandamods.extra_details.utils.animation.CurveRamp;
import me.pandamods.extra_details.utils.animation.KeyPoint;
import me.pandamods.extra_details.utils.animation.KeyType;
import me.pandamods.pandalib.utils.VectorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class DoorRenderer implements BlockEntityRenderer<DoorEntity> {
	final BlockRenderDispatcher blockRender;

	public DoorRenderer(BlockEntityRendererProvider.Context context) {
		this.blockRender = context.getBlockRenderDispatcher();
		ExtraDetails.LOGGER.error("created renderer");
	}

	public static final CurveRamp doorAnimation = new CurveRamp(
			new KeyPoint(KeyType.CATMULL_ROM, new Vector2f(0, 0)),
			new KeyPoint(KeyType.LINEAR, new Vector2f(0.5f, 1)),
			new KeyPoint(KeyType.CATMULL_ROM, new Vector2f(0.75f, 0.95f)),
			new KeyPoint(KeyType.LINEAR, new Vector2f(1, 1))
	);

	@Override
	public void render(DoorEntity blockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
		ExtraDetails.LOGGER.error("im rendering");
		BlockState blockState = blockEntity.getBlockState();
		BlockState state = blockEntity.getBlockState().getBlock().defaultBlockState()
				.setValue(DoorBlock.HALF, blockState.getValue(DoorBlock.HALF));

		float speed = RenderUtils.getDeltaSeconds() / ExtraDetails.getConfig().door_animation_length;
		blockEntity.openingTime = Math.clamp(0, 1, blockEntity.openingTime + (blockState.getValue(DoorBlock.OPEN) ? speed : -speed));

		Vector3f pivot = new Vector3f(1.5f, 0, 14.5f).div(16);

		poseStack.pushPose();
		VectorUtils.rotateByPivot(
				poseStack,
				new Vector3f(0.5f, 0, 0.5f),
				VectorUtils.toRadians(new Vector3f(0, -blockState.getValue(DoorBlock.FACING).toYRot() + 180, 0))
		);
		VectorUtils.rotateByPivot(
				poseStack,
				new Vector3f(0.5f, pivot.y, pivot.z),
				VectorUtils.toRadians(new Vector3f(0, blockState.getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT ? 180 : 0, 0))
		);

		float openMaxRotation = blockState.getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT ? -90 : 90;
		float animValue = blockState.getValue(DoorBlock.OPEN) ?
				doorAnimation.getValue(blockEntity.openingTime) :
				1 - doorAnimation.getValue(1 - blockEntity.openingTime);

		VectorUtils.rotateByPivot(
				poseStack,
				new Vector3f(pivot.x, pivot.y, pivot.z),
				new Vector3f(
						0,
						Math.toRadians(animValue * openMaxRotation),
						0
				)
		);

		RenderUtils.renderBlock(poseStack, state, multiBufferSource, i, j);
		poseStack.popPose();
	}
}

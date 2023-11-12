package me.pandamods.extra_details.client.renderer.block.sign;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.model.block.sign.HangingSignModel;
import me.pandamods.extra_details.entity.block.HangingSignClientBlock;
import me.pandamods.pandalib.client.model.Bone;
import me.pandamods.pandalib.client.render.block.extensions.MeshClientBlockRenderer;
import me.pandamods.pandalib.utils.VectorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.HangingSignBlockEntity;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

public class SwingingHangingSignRenderer extends MeshClientBlockRenderer<HangingSignClientBlock, HangingSignModel> {
	public SwingingHangingSignRenderer() {
		super(new HangingSignModel());
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public boolean enabled(BlockState state) {
		return ExtraDetails.getConfig().blockSettings.hangingSign.enabled && ExtraDetails.getConfig().isAllowed(state.getBlock());
	}

	@Override
	public void render(HangingSignClientBlock block, PoseStack poseStack, MultiBufferSource buffer, int lightColor, int overlay, float partialTick) {
		super.render(block, poseStack, buffer, lightColor, overlay, partialTick);

		BlockState blockState = block.getBlockState();
		BlockPos blockPos = block.getBlockPos();
		if (blockState.hasBlockEntity() && block.getLevel().getBlockEntity(blockPos) instanceof HangingSignBlockEntity blockEntity) {
			BlockEntityRenderer<SignBlockEntity> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(blockEntity);
			if (renderer instanceof HangingSignRenderer signRenderer) {
				Bone bone;
				if (block.getCache().armature != null && (bone = block.getCache().armature.getBone("chains").orElse(null)) != null) {
					poseStack.pushPose();
					VectorUtils.rotateByPivot(poseStack,
							new Vector3f(.5f, 0, .5f), new Vector3f(0, (float) Math.toRadians(this.getYRotation(blockState)), 0));

					bone.applyToPoseStack(poseStack);
//					LevelRenderer.renderLineBox(poseStack, buffer.getBuffer(RenderType.LINES), new AABB(0, 0, 0, 0, 0, 1), 0, 0, 1, 1);
					poseStack.translate(0, -6f/16f, 0);
//					LevelRenderer.renderLineBox(poseStack, buffer.getBuffer(RenderType.LINES), new AABB(0, 0, 0, 0, 0, 1), 1, 0, 0, 1);

					signRenderer.renderSignText(blockEntity.getBlockPos(), blockEntity.getFrontText(), poseStack, buffer, lightColor,
							blockEntity.getTextLineHeight(), blockEntity.getMaxTextLineWidth(), true);
					signRenderer.renderSignText(blockEntity.getBlockPos(), blockEntity.getBackText(), poseStack, buffer, lightColor,
							blockEntity.getTextLineHeight(), blockEntity.getMaxTextLineWidth(), false);

					poseStack.popPose();
				}
			}
		}
	}
}

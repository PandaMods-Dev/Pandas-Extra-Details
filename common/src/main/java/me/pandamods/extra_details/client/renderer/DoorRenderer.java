package me.pandamods.extra_details.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.extra_details.api.clientblockentity.renderer.ClientBlockEntityRenderer;
import me.pandamods.extra_details.client.animationcontroller.DoorAnimationController;
import me.pandamods.extra_details.client.clientblockentity.DoorBlockEntity;
import me.pandamods.pandalib.client.armature.Armature;
import me.pandamods.pandalib.client.armature.ArmatureAnimator;
import me.pandamods.pandalib.client.mesh.MeshBlockEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;

public class DoorRenderer implements ClientBlockEntityRenderer<DoorBlockEntity>, ArmatureAnimator<DoorBlockEntity, DoorAnimationController> {
	private final DoorAnimationController animationController = new DoorAnimationController();

	@Override
	public void render(DoorBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor) {
		BlockRenderDispatcher blockRenderDispatcher = Minecraft.getInstance().getBlockRenderer();

		poseStack.pushPose();
		MeshBlockEntityRenderer.translateBlock(blockEntity.getBlockState(), poseStack);
		poseStack.translate(.5f, 0, .5f);
		poseStack.mulPose(Axis.YP.rotationDegrees(180));
		poseStack.translate(-.5f, 0, -.5f);
		animateArmature(blockEntity, partialTick);
		Armature armature = blockEntity.animatableCache().armature;
		if (armature != null && blockEntity.hasLevel()) {
			BlockState blockState = blockEntity.getBlockState();
			BlockState state = blockEntity.getBlockState().getBlock().defaultBlockState()
					.setValue(DoorBlock.HALF, blockState.getValue(DoorBlock.HALF))
					.setValue(DoorBlock.HINGE, blockState.getValue(DoorBlock.HINGE));
			armature.getOptionalBone("door").ifPresent(bone ->
					blockRenderDispatcher.renderBatched(state,
							blockEntity.getBlockPos(), blockEntity.getLevel(), bone.applyToPoseStack(poseStack),
							bufferSource.getBuffer(RenderType.cutout()), false, blockEntity.getLevel().getRandom()));
		}
		poseStack.popPose();
	}

	@Override
	public DoorAnimationController getController() {
		return this.animationController;
	}
}

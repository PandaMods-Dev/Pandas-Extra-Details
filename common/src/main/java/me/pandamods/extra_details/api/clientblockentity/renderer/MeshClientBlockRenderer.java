package me.pandamods.extra_details.api.clientblockentity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.pandalib.client.Model;
import me.pandamods.pandalib.client.animation.AnimationController;
import me.pandamods.pandalib.client.armature.ArmatureAnimator;
import me.pandamods.pandalib.client.armature.IAnimatable;
import me.pandamods.pandalib.client.mesh.MeshBlockEntityRenderer;
import me.pandamods.pandalib.client.mesh.MeshRenderer;
import net.minecraft.client.renderer.MultiBufferSource;

public abstract class MeshClientBlockRenderer<T extends ClientBlockEntity & IAnimatable, M extends Model<T>, AC extends AnimationController<T>>
		implements ClientBlockEntityRenderer<T>, MeshRenderer<T, M>, ArmatureAnimator<T, AC> {
	private final M model;
	private final AC animationController;

	public MeshClientBlockRenderer(M model, AC animationController) {
		this.model = model;
		this.animationController = animationController;
	}

	@Override
	public void render(T blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor) {
		poseStack.pushPose();
		MeshBlockEntityRenderer.translateBlock(blockEntity.getBlockState(), poseStack);
		animateArmature(blockEntity, partialTick);
		renderGeometry(blockEntity, blockEntity.animatableCache().armature, poseStack, bufferSource, lightColor);
		poseStack.popPose();
	}

	@Override
	public M getModel() {
		return this.model;
	}

	@Override
	public AC getController() {
		return this.animationController;
	}
}

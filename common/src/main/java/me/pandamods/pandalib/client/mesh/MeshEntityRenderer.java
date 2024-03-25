package me.pandamods.pandalib.client.mesh;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.pandalib.client.Model;
import me.pandamods.pandalib.client.animation.AnimationController;
import me.pandamods.pandalib.client.armature.ArmatureAnimator;
import me.pandamods.pandalib.client.armature.IAnimatable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public abstract class MeshEntityRenderer<T extends Entity & IAnimatable, M extends Model<T>, AC extends AnimationController<T>>
		extends EntityRenderer<T> implements MeshRenderer<T, M>, ArmatureAnimator<T, AC> {
	private final M model;
	private final AC animationController;

	protected MeshEntityRenderer(EntityRendererProvider.Context context, M model, AC animationController) {
		super(context);
		this.model = model;
		this.animationController = animationController;
	}

	@Override
	public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
		super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
		animateArmature(entity, partialTick);
		renderGeometry(entity, entity.animatableCache().armature, poseStack, buffer, packedLight);
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

package me.pandamods.extra_details.client.renderer;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.*;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.api.blockdata.BlockData;
import me.pandamods.extra_details.api.render.MeshBlockRenderer;
import me.pandamods.pandalib.client.animation.Animatable;
import me.pandamods.pandalib.client.animation.AnimatableInstance;
import me.pandamods.pandalib.client.animation.states.AnimationController;
import me.pandamods.pandalib.client.animation.states.AnimationState;
import me.pandamods.pandalib.client.animation.states.State;
import me.pandamods.pandalib.utils.MatrixUtils;
import me.pandamods.pandalib.resource.Mesh;
import me.pandamods.pandalib.resource.AssimpResources;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;

public class LeverRenderer implements MeshBlockRenderer<LeverRenderer.LeverData>, AnimationController<LeverRenderer.LeverData> {
	private final Mesh mesh = AssimpResources.getMesh(ExtraDetails.ID("assimp/meshes/block/redstone/lever.fbx"));

	@Override
	public void render(BlockPos blockPos, ClientLevel level, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor) {
		poseStack.pushPose();
		MatrixUtils.translateBlock(level.getBlockState(blockPos), poseStack);
		MeshBlockRenderer.super.render(blockPos, level, poseStack, bufferSource, partialTick, lightColor);
		poseStack.popPose();
	}

	@Override
	public Mesh getMesh(ClientLevel level, BlockPos blockPos) {
		return mesh;
	}

	@Override
	public AnimationController<LeverData> getAnimationController(ClientLevel level, BlockPos blockPos) {
		return this;
	}

	@Override
	public LeverData getData(ClientLevel level, BlockPos blockPos) {
		return level.extraDetails$getBlockData(blockPos, LeverData::new);
	}

	@Override
	public State registerStates(LeverData leverData) {
		State offState = new AnimationState(ExtraDetails.ID("assimp/animations/block/redstone/lever_off.fbx"));
		State onState = new AnimationState(ExtraDetails.ID("assimp/animations/block/redstone/lever_on.fbx"));

		offState.nextTransitionState(() -> leverData.getBlockstate().getValue(LeverBlock.POWERED), onState, .1f);
		onState.nextTransitionState(() -> !leverData.getBlockstate().getValue(LeverBlock.POWERED), offState, .1f);

		return leverData.getBlockstate().getValue(LeverBlock.POWERED) ? onState : offState;
	}

	public static class LeverData extends BlockData implements Animatable {
		private final AnimatableInstance animatableInstance = new AnimatableInstance(this);

		public LeverData(BlockPos blockPos, Level level) {
			super(blockPos, level);
		}

		@Override
		public AnimatableInstance getAnimatableInstance() {
			return this.animatableInstance;
		}

		@Override
		public float getTick() {
			return (float) (Blaze3D.getTime() * 20);
		}
	}
}

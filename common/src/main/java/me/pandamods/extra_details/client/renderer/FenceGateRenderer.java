/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.extra_details.client.renderer;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.api.blockdata.BlockData;
import me.pandamods.extra_details.api.render.MeshBlockRenderer;
import me.pandamods.pandalib.api.model.client.animation.Animatable;
import me.pandamods.pandalib.api.model.client.animation.AnimatableInstance;
import me.pandamods.pandalib.api.model.client.animation.states.AnimationController;
import me.pandamods.pandalib.api.model.client.animation.states.AnimationState;
import me.pandamods.pandalib.api.model.client.animation.states.State;
import me.pandamods.pandalib.api.model.resource.AssimpResources;
import me.pandamods.pandalib.api.model.resource.model.Model;
import me.pandamods.pandalib.utils.BlockUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;

public class FenceGateRenderer implements MeshBlockRenderer<FenceGateRenderer.LeverData>, AnimationController<FenceGateRenderer.LeverData> {
	private final Model model = AssimpResources.getModel(ExtraDetails.LOCATION("assimp/meshes/block/fence_gate/fence_gate.fbx"));

	@Override
	public void render(BlockPos blockPos, ClientLevel level, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor) {
		poseStack.pushPose();
		BlockUtils.translateBlock(level.getBlockState(blockPos), poseStack);
		if (level.getBlockState(blockPos).getValue(FenceGateBlock.IN_WALL))
			poseStack.translate(0, -3f /16, 0);
		poseStack.mulPose(Axis.YP.rotationDegrees(180));
		MeshBlockRenderer.super.render(blockPos, level, poseStack, bufferSource, partialTick, lightColor);
		poseStack.popPose();
	}

	@Override
	public ResourceLocation getTexture(ClientLevel level, BlockPos blockPos, String textureName) {
		BlockState blockState = level.getBlockState(blockPos);
		ResourceLocation resourceLocation = blockState.getBlock().arch$registryName();
		return ResourceLocation.fromNamespaceAndPath(resourceLocation.getNamespace(), "block/" +
				resourceLocation.getPath().replace("fence_gate", "planks"));
	}

	@Override
	public Model getMesh(ClientLevel level, BlockPos blockPos) {
		return model;
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
		State offState = new AnimationState(ExtraDetails.LOCATION("assimp/animations/block/fence_gate/fence_gate_close.fbx"));
		State onState = new AnimationState(ExtraDetails.LOCATION("assimp/animations/block/fence_gate/fence_gate_open.fbx"));

		offState.nextTransitionState(() -> leverData.getBlockstate().getValue(FenceGateBlock.OPEN), onState, .1f);
		onState.nextTransitionState(() -> !leverData.getBlockstate().getValue(FenceGateBlock.OPEN), offState, .1f);

		return leverData.getBlockstate().getValue(FenceGateBlock.OPEN) ? onState : offState;
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

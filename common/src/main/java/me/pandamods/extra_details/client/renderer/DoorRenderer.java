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
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class DoorRenderer implements MeshBlockRenderer<DoorRenderer.DoorData>, AnimationController<DoorRenderer.DoorData> {
	private final Model leftModel = AssimpResources.getModel(ExtraDetails.ID("assimp/meshes/block/door/door_left.fbx"));
	private final Model rightModel = AssimpResources.getModel(ExtraDetails.ID("assimp/meshes/block/door/door_right.fbx"));

	@Override
	public void render(BlockPos blockPos, ClientLevel level, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightmapUV) {
		poseStack.pushPose();
		BlockUtils.translateBlock(level.getBlockState(blockPos), poseStack);
		poseStack.mulPose(Axis.YP.rotationDegrees(180));
		MeshBlockRenderer.super.render(blockPos, level, poseStack, bufferSource, partialTick, lightmapUV);
		poseStack.popPose();
	}

	@Override
	public ResourceLocation getTexture(ClientLevel level, BlockPos blockPos, String textureName) {
		BlockState blockState = level.getBlockState(blockPos);
		boolean isTop = blockState.getValue(DoorBlock.HALF).equals(DoubleBlockHalf.UPPER);
		ResourceLocation resourceLocation = blockState.getBlock().arch$registryName();
		return resourceLocation.withPrefix("block/").withSuffix(String.format("_%s", isTop ? "top" : "bottom"));
	}

	@Override
	public Model getMesh(ClientLevel level, BlockPos blockPos) {
		BlockState blockState = level.getBlockState(blockPos);
		boolean isTop = blockState.getValue(DoorBlock.HALF).equals(DoubleBlockHalf.UPPER);

		leftModel.getRootNode().findNode("mesh_top").setVisible(isTop);
		rightModel.getRootNode().findNode("mesh_top").setVisible(isTop);

		leftModel.getRootNode().findNode("mesh_bottom").setVisible(!isTop);
		rightModel.getRootNode().findNode("mesh_bottom").setVisible(!isTop);

		boolean isRight = blockState.getValue(DoorBlock.HINGE).equals(DoorHingeSide.RIGHT);
		return isRight ? rightModel : leftModel;
	}

	@Override
	public AnimationController<DoorData> getAnimationController(ClientLevel level, BlockPos blockPos) {
		return this;
	}

	@Override
	public DoorData getData(ClientLevel level, BlockPos blockPos) {
		return level.extraDetails$getBlockData(blockPos, DoorData::new);
	}

	@Override
	public State registerStates(DoorData doorData) {
		boolean isRight = doorData.getBlockstate().getValue(DoorBlock.HINGE).equals(DoorHingeSide.RIGHT);
		State offState = new AnimationState(ExtraDetails.ID(isRight ?
				"assimp/animations/block/door/door_right_close.fbx" : "assimp/animations/block/door/door_left_close.fbx"));
		State onState = new AnimationState(ExtraDetails.ID(isRight ?
				"assimp/animations/block/door/door_right_open.fbx" : "assimp/animations/block/door/door_left_open.fbx"));

		offState.nextTransitionState(() -> doorData.getBlockstate().getValue(DoorBlock.OPEN), onState, .1f);
		onState.nextTransitionState(() -> !doorData.getBlockstate().getValue(DoorBlock.OPEN), offState, .1f);

		return doorData.getBlockstate().getValue(DoorBlock.OPEN) ? onState : offState;
	}

	public static class DoorData extends BlockData implements Animatable {
		private final AnimatableInstance animatableInstance = new AnimatableInstance(this);

		public DoorData(BlockPos blockPos, Level level) {
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

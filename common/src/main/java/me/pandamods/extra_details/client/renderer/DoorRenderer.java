package me.pandamods.extra_details.client.renderer;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.api.blockdata.BlockData;
import me.pandamods.extra_details.api.render.MeshBlockRenderer;
import me.pandamods.pandalib.client.animation.Animatable;
import me.pandamods.pandalib.client.animation.AnimatableInstance;
import me.pandamods.pandalib.client.animation.states.AnimationController;
import me.pandamods.pandalib.client.animation.states.AnimationState;
import me.pandamods.pandalib.client.animation.states.State;
import me.pandamods.pandalib.resource.AssimpResources;
import me.pandamods.pandalib.resource.Mesh;
import me.pandamods.pandalib.utils.MatrixUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class DoorRenderer implements MeshBlockRenderer<DoorRenderer.DoorData>, AnimationController<DoorRenderer.DoorData> {
	private final Mesh topMesh = AssimpResources.getMesh(ExtraDetails.ID("assimp/meshes/block/door/door_top.fbx"));
	private final Mesh bottomMesh = AssimpResources.getMesh(ExtraDetails.ID("assimp/meshes/block/door/door_bottom.fbx"));

	@Override
	public void render(BlockPos blockPos, ClientLevel level, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightmapUV) {
		poseStack.pushPose();
		MatrixUtils.translateBlock(level.getBlockState(blockPos), poseStack);
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
	public Mesh getMesh(ClientLevel level, BlockPos blockPos) {
		BlockState blockState = level.getBlockState(blockPos);
		boolean isTop = blockState.getValue(DoorBlock.HALF).equals(DoubleBlockHalf.UPPER);
		return isTop ? topMesh : bottomMesh;
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
		State offState = new AnimationState(ExtraDetails.ID("assimp/animations/block/door/door_close.fbx"));
		State onState = new AnimationState(ExtraDetails.ID("assimp/animations/block/door/door_open.fbx"));

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

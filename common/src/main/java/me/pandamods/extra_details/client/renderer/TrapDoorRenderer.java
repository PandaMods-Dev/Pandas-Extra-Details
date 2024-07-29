package me.pandamods.extra_details.client.renderer;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.api.blockdata.BlockData;
import me.pandamods.extra_details.api.render.MeshBlockRenderer;
import me.pandamods.pandalib.client.animation.Animatable;
import me.pandamods.pandalib.client.animation.AnimatableInstance;
import me.pandamods.pandalib.client.animation.states.AnimationController;
import me.pandamods.pandalib.client.animation.states.AnimationState;
import me.pandamods.pandalib.client.animation.states.State;
import me.pandamods.pandalib.resource.AssimpResources;
import me.pandamods.pandalib.resource.model.Model;
import me.pandamods.pandalib.utils.BlockUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

public class TrapDoorRenderer implements MeshBlockRenderer<TrapDoorRenderer.TrapDoorData>, AnimationController<TrapDoorRenderer.TrapDoorData> {
	private final Model topModel = AssimpResources.getModel(ExtraDetails.ID("assimp/meshes/block/trapdoor/trap_door_top.fbx"));
	private final Model bottomModel = AssimpResources.getModel(ExtraDetails.ID("assimp/meshes/block/trapdoor/trap_door_bottom.fbx"));

	@Override
	public void render(BlockPos blockPos, ClientLevel level, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightmapUV) {
		poseStack.pushPose();
		BlockUtils.translateBlock(level.getBlockState(blockPos), poseStack);
		MeshBlockRenderer.super.render(blockPos, level, poseStack, bufferSource, partialTick, lightmapUV);
		poseStack.popPose();
	}

	@Override
	public ResourceLocation getTexture(ClientLevel level, BlockPos blockPos, String textureName) {
		BlockState blockState = level.getBlockState(blockPos);
		ResourceLocation resourceLocation = blockState.getBlock().arch$registryName();
		return resourceLocation.withPrefix("block/");
	}

	@Override
	public Model getMesh(ClientLevel level, BlockPos blockPos) {
		BlockState blockState = level.getBlockState(blockPos);
		boolean isTop = blockState.getValue(TrapDoorBlock.HALF).equals(Half.TOP);
		return isTop ? topModel : bottomModel;
	}

	@Override
	public AnimationController<TrapDoorData> getAnimationController(ClientLevel level, BlockPos blockPos) {
		return this;
	}

	@Override
	public TrapDoorData getData(ClientLevel level, BlockPos blockPos) {
		return level.extraDetails$getBlockData(blockPos, TrapDoorData::new);
	}

	@Override
	public State registerStates(TrapDoorData trapDoorData) {
		boolean isTop = trapDoorData.getBlockstate().getValue(TrapDoorBlock.HALF).equals(Half.TOP);
		State offState = new AnimationState(ExtraDetails.ID(isTop ?
				"assimp/animations/block/trapdoor/trap_door_close_top.fbx" : "assimp/animations/block/trapdoor/trap_door_close_bottom.fbx"));
		State onState = new AnimationState(ExtraDetails.ID(isTop ?
				"assimp/animations/block/trapdoor/trap_door_open_top.fbx" : "assimp/animations/block/trapdoor/trap_door_open_bottom.fbx"));

		offState.nextTransitionState(() -> trapDoorData.getBlockstate().getValue(TrapDoorBlock.OPEN), onState, .1f);
		onState.nextTransitionState(() -> !trapDoorData.getBlockstate().getValue(TrapDoorBlock.OPEN), offState, .1f);

		return trapDoorData.getBlockstate().getValue(TrapDoorBlock.OPEN) ? onState : offState;
	}

	public static class TrapDoorData extends BlockData implements Animatable {
		private final AnimatableInstance animatableInstance = new AnimatableInstance(this);

		public TrapDoorData(BlockPos blockPos, Level level) {
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

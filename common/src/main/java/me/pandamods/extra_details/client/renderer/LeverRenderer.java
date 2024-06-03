package me.pandamods.extra_details.client.renderer;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.api.blockdata.BlockData;
import me.pandamods.extra_details.api.render.BlockRenderer;
import me.pandamods.pandalib.client.animation.Animatable;
import me.pandamods.pandalib.client.animation.AnimatableInstance;
import me.pandamods.pandalib.client.animation.states.AnimationController;
import me.pandamods.pandalib.client.animation.states.AnimationState;
import me.pandamods.pandalib.client.animation.states.State;
import me.pandamods.pandalib.utils.MatrixUtils;
import me.pandamods.pandalib.utils.PLSpriteCoordinateExpander;
import me.pandamods.pandalib.client.render.PLRenderTypes;
import me.pandamods.pandalib.resource.Mesh;
import me.pandamods.pandalib.resource.AssimpResources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;

public class LeverRenderer implements BlockRenderer, AnimationController<LeverRenderer.LeverData> {
	private final Mesh mesh = AssimpResources.getMesh(new ResourceLocation(ExtraDetails.MOD_ID, "assimp/meshes/block/redstone/lever.fbx"));

	@Override
	public void render(BlockPos blockPos, ClientLevel level, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor) {
		LeverData data = level.extraDetails$getBlockData(blockPos, LeverData::new);

		poseStack.pushPose();
		MatrixUtils.translateBlock(data.getBlockstate(), poseStack);
		poseStack.mulPose(Axis.XP.rotationDegrees(-90));

		animate(data, mesh, partialTick);

		TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(new ResourceLocation("textures/atlas/blocks.png"));
		mesh.render(poseStack.last().pose(), poseStack.last().normal(), OverlayTexture.NO_OVERLAY, lightColor, s -> new PLSpriteCoordinateExpander(
				bufferSource.getBuffer(PLRenderTypes.cutoutMesh()),
				atlas.getSprite(new ResourceLocation("block/" + s))
		));
		poseStack.popPose();
	}

	@Override
	public State registerStates(LeverData leverData) {
		State offState = new AnimationState(new ResourceLocation(ExtraDetails.MOD_ID, "assimp/animations/block/redstone/lever_off.fbx"), true);
		State onState = new AnimationState(new ResourceLocation(ExtraDetails.MOD_ID, "assimp/animations/block/redstone/lever_on.fbx"));

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

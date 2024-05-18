package me.pandamods.extra_details.client.renderer;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import me.pandamods.extra_details.api.blockdata.BlockData;
import me.pandamods.extra_details.api.render.BlockRenderer;
import me.pandamods.extra_details.utils.PLSpriteCoordinateExpander;
import me.pandamods.pandalib.client.render.PLRenderTypes;
import me.pandamods.pandalib.core.utils.RenderUtils;
import me.pandamods.pandalib.resource.Mesh;
import me.pandamods.pandalib.resource.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class LeverRenderer implements BlockRenderer {
	@Override
	public void render(BlockPos blockPos, ClientLevel level, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor) {
		poseStack.pushPose();
		poseStack.translate(.5f, 0, .5f);
		poseStack.mulPose(Axis.XP.rotationDegrees(-90));

		LeverData data = level.extraDetails$getBlockData(blockPos, LeverData::new);
		Mesh mesh = Resources.getMesh(new ResourceLocation("extra_details", "pandalib/meshes/block/redstone/lever.fbx"));

		data.value += RenderUtils.getDeltaSeconds();
		Mesh.Bone bone = mesh.getBone("handle");
		bone.getLocalMatrix().setRotationXYZ((float) Math.toRadians(data.value), 0, 0);

		TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(new ResourceLocation("textures/atlas/blocks.png"));
		mesh.render(poseStack.last().pose(), poseStack.last().normal(), OverlayTexture.NO_OVERLAY, lightColor, s -> new PLSpriteCoordinateExpander(
				bufferSource.getBuffer(PLRenderTypes.cutoutMesh()),
				atlas.getSprite(new ResourceLocation("block/" + s))
		));
		poseStack.popPose();
	}

	public static class LeverData extends BlockData {
		public float value = 0;

		public LeverData(BlockPos blockPos, Level level) {
			super(blockPos, level);
		}
	}
}

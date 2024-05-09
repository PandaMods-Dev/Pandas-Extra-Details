package me.pandamods.extra_details.client.renderer;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import me.pandamods.extra_details.api.clientblockentity.renderer.ClientBlockEntityRenderer;
import me.pandamods.extra_details.client.clientblockentity.LeverBlockEntity;
import me.pandamods.extra_details.utils.PLSpriteCoordinateExpander;
import me.pandamods.pandalib.client.render.PLRenderTypes;
import me.pandamods.pandalib.resource.Mesh;
import me.pandamods.pandalib.resource.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class LeverRenderer implements ClientBlockEntityRenderer<LeverBlockEntity> {
	private final Mesh mesh = Resources.getMesh(new ResourceLocation("extra_details", "pandalib/meshes/block/redstone/lever.fbx"));
	private final TextureAtlas atlas = Minecraft.getInstance().getModelManager().getAtlas(new ResourceLocation("textures/atlas/blocks.png"));

	@Override
	public void render(LeverBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor) {
		poseStack.pushPose();
		poseStack.translate(.5f, 0, .5f);
		poseStack.mulPose(Axis.XP.rotationDegrees(-90));

		mesh.render(poseStack.last().pose(), poseStack.last().normal(), OverlayTexture.NO_OVERLAY, lightColor, s -> new PLSpriteCoordinateExpander(
				bufferSource.getBuffer(PLRenderTypes.cutoutTriangular()),
				atlas.getSprite(new ResourceLocation("block/" + s))
		));
		poseStack.popPose();
	}
}

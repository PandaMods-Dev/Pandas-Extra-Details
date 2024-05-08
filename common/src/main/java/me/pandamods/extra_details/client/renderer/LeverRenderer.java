package me.pandamods.extra_details.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.api.clientblockentity.renderer.ClientBlockEntityRenderer;
import me.pandamods.extra_details.api.clientblockentity.renderer.MeshClientBlockRenderer;
import me.pandamods.extra_details.client.animationcontroller.LeverAnimationController;
import me.pandamods.extra_details.client.clientblockentity.LeverBlockEntity;
import me.pandamods.extra_details.client.model.LeverModel;
import me.pandamods.pandalib.resource.Mesh;
import me.pandamods.pandalib.resource.Resources;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class LeverRenderer implements ClientBlockEntityRenderer<LeverBlockEntity> {
	@Override
	public void render(LeverBlockEntity blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor) {
		Mesh mesh = Resources.getMesh(new ResourceLocation("extra_details", "pandalib/meshes/monkey.fbx"));
		mesh.render(bufferSource, poseStack.last().pose(), poseStack.last().normal(), OverlayTexture.NO_OVERLAY, lightColor);
	}
}

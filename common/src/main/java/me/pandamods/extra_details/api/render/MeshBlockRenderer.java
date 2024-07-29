package me.pandamods.extra_details.api.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.api.blockdata.BlockData;
import me.pandamods.pandalib.client.animation.Animatable;
import me.pandamods.pandalib.client.animation.states.AnimationController;
import me.pandamods.pandalib.client.render.PLRenderType;
import me.pandamods.pandalib.client.render.ModelRenderer;
import me.pandamods.pandalib.resource.model.Model;
import me.pandamods.pandalib.utils.PLSpriteCoordinateExpander;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public interface MeshBlockRenderer<T extends BlockData & Animatable> extends BlockRenderer {
	@Override
	default void render(BlockPos blockPos, ClientLevel level, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightmapUV) {
		poseStack.pushPose();
		Model model = getMesh(level, blockPos);

		getAnimationController(level, blockPos).animate(getData(level, blockPos), model, partialTick);

		TextureAtlas atlas = Minecraft.getInstance().getModelManager()
				.getAtlas(ResourceLocation.withDefaultNamespace("textures/atlas/blocks.png"));
		VertexConsumer vertexConsumer = bufferSource.getBuffer(PLRenderType.CUTOUT_MESH);
		ModelRenderer.render(model, poseStack, OverlayTexture.NO_OVERLAY, lightmapUV, s -> new PLSpriteCoordinateExpander(
						vertexConsumer, atlas.getSprite(getTexture(level, blockPos, s))
		));
		//Todo Debugging
		ModelRenderer.renderModelDebug(model, poseStack, bufferSource);
		poseStack.popPose();
	}

	default ResourceLocation getTexture(ClientLevel level, BlockPos blockPos, String textureName) {
		return ResourceLocation.tryParse(textureName).withPrefix("block/");
	}
	Model getMesh(ClientLevel level, BlockPos blockPos);
	AnimationController<T> getAnimationController(ClientLevel level, BlockPos blockPos);
	T getData(ClientLevel level, BlockPos blockPos);
}

package me.pandamods.extra_details.api.render;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.api.blockdata.BlockData;
import me.pandamods.pandalib.client.animation.Animatable;
import me.pandamods.pandalib.client.animation.states.AnimationController;
import me.pandamods.pandalib.client.render.PLRenderType;
import me.pandamods.pandalib.resource.Mesh;
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
		Mesh mesh = getMesh(level, blockPos);

		getAnimationController(level, blockPos).animate(getData(level, blockPos), mesh, partialTick);

		TextureAtlas atlas = Minecraft.getInstance().getModelManager()
				.getAtlas(ResourceLocation.withDefaultNamespace("textures/atlas/blocks.png"));
		mesh.render(poseStack, OverlayTexture.NO_OVERLAY, lightmapUV, s -> new PLSpriteCoordinateExpander(
				bufferSource.getBuffer(PLRenderType.CUTOUT_MESH),
				atlas.getSprite(getTexture(level, blockPos, s))
		));
		poseStack.popPose();
	}

	default ResourceLocation getTexture(ClientLevel level, BlockPos blockPos, String textureName) {
		return ResourceLocation.tryParse(textureName).withPrefix("block/");
	}
	Mesh getMesh(ClientLevel level, BlockPos blockPos);
	AnimationController<T> getAnimationController(ClientLevel level, BlockPos blockPos);
	T getData(ClientLevel level, BlockPos blockPos);
}

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

package me.pandamods.extra_details.api.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import me.pandamods.extra_details.api.blockdata.BlockData;
import me.pandamods.pandalib.api.model.client.animation.Animatable;
import me.pandamods.pandalib.api.model.client.animation.states.AnimationController;
import me.pandamods.pandalib.api.model.client.render.PLRenderType;
import me.pandamods.pandalib.api.model.client.render.ModelRenderer;
import me.pandamods.pandalib.api.model.resource.model.Model;
import me.pandamods.pandalib.utils.PLSpriteCoordinateExpander;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
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
		VertexConsumer vertexConsumer = bufferSource.getBuffer(PLRenderType.CUTOUT_MESH_ENTITY.apply(atlas.location()));
		ModelRenderer.render(model, poseStack, OverlayTexture.NO_OVERLAY, lightmapUV, s -> new PLSpriteCoordinateExpander(
						vertexConsumer, atlas.getSprite(getTexture(level, blockPos, s))
		));
		poseStack.popPose();
	}

	default ResourceLocation getTexture(ClientLevel level, BlockPos blockPos, String textureName) {
		return ResourceLocation.tryParse(textureName).withPrefix("block/");
	}
	Model getMesh(ClientLevel level, BlockPos blockPos);
	AnimationController<T> getAnimationController(ClientLevel level, BlockPos blockPos);
	T getData(ClientLevel level, BlockPos blockPos);
}

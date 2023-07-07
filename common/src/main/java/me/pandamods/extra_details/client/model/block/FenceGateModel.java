package me.pandamods.extra_details.client.model.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.FenceGateEntity;
import me.pandamods.extra_details.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RenderShape;
import software.bernie.geckolib.model.GeoModel;

import java.util.List;

public class FenceGateModel extends GeoModel<FenceGateEntity> {
	@Override
	public ResourceLocation getModelResource(FenceGateEntity animatable) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "geo/fence_gate.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(FenceGateEntity animatable) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(animatable.getBlockState());
		ResourceLocation resourceLocation = textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
	}

	@Override
	public ResourceLocation getAnimationResource(FenceGateEntity animatable) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "animations/fence_gate.animation.json");
	}
}

package me.pandamods.extra_details.client.model.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.FenceGateEntity;
import me.pandamods.extra_details.entity.block.LeverEntity;
import me.pandamods.extra_details.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LeverModel extends GeoModel<LeverEntity> {
	@Override
	public ResourceLocation getModelResource(LeverEntity animatable) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "geo/lever.geo.json");
	}

	@Override
	public ResourceLocation getTextureResource(LeverEntity animatable) {
		return getTextureResource(animatable, false);
	}

	public ResourceLocation getTextureResource(LeverEntity animatable, boolean bl) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(animatable.getBlockState());
		ResourceLocation resourceLocation = bl && textures.size() > 1 ? textures.get(1) : textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
	}

	@Override
	public ResourceLocation getAnimationResource(LeverEntity animatable) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "animations/lever.animation.json");
	}
}

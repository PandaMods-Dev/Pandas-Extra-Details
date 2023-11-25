package me.pandamods.extra_details.client.model.block.redstone;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.animation_controller.block.redstone.LeverAnimationController;
import me.pandamods.extra_details.entity.block.LeverClientBlock;
import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.utils.RenderUtils;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class LeverModel implements MeshModel<LeverClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(LeverClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/redstone/lever.json");
//		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/suzanne.json");
	}

	@Override
	public ResourceLocation getTextureLocation(String textureName, LeverClientBlock base) {
		return switch (textureName) {
			case "lever" -> getTextureResource(base, true);
			default -> getTextureResource(base, false);
		};
//		return new ResourceLocation(ExtraDetails.MOD_ID, "textures/suzanne.png");
	}

	public ResourceLocation getTextureResource(LeverClientBlock animatable, boolean bl) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(animatable.getBlockState());
		ResourceLocation resourceLocation = bl && textures.size() > 1 ? textures.get(1) : textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
	}

	@Override
	public AnimationControllerProvider<LeverClientBlock> createAnimationController() {
		return LeverAnimationController::new;
//		return null;
	}
}

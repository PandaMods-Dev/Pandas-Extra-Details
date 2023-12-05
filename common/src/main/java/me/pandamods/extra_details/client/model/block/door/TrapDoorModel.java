package me.pandamods.extra_details.client.model.block.door;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.animation_controller.block.door.TrapDoorAnimationController;
import me.pandamods.extra_details.entity.block.TrapDoorClientBlock;
import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.pandalib.utils.RenderUtils;
import me.pandamods.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class TrapDoorModel implements MeshModel<TrapDoorClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(TrapDoorClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/door/trap_door.json");
	}

	@Override
	public ResourceLocation getTextureLocation(String textureName, TrapDoorClientBlock base) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(base.getBlockState());
		ResourceLocation resourceLocation = textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
	}

	@Override
	public AnimationControllerProvider<TrapDoorClientBlock> createAnimationController(TrapDoorClientBlock base) {
		return TrapDoorAnimationController::new;
	}
}

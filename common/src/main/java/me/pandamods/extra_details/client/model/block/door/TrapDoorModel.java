package me.pandamods.extra_details.client.model.block.door;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.animation_controller.block.door.TrapDoorAnimationController;
import me.pandamods.extra_details.entity.block.FenceGateClientBlock;
import me.pandamods.extra_details.entity.block.TrapDoorClientBlock;
import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.pandalib.utils.RenderUtils;
import me.pandamods.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.include.com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

public class TrapDoorModel implements MeshModel<TrapDoorClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(TrapDoorClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/door/trap_door.json");
	}

	@Override
	public Map<String, ResourceLocation> getTextureLocations(TrapDoorClientBlock base) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(base.getBlockState());
		ResourceLocation resourceLocation = textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			resourceLocation = new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		else
			resourceLocation = new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
		return Map.of("", resourceLocation);
	}

	@Override
	public AnimationControllerProvider<TrapDoorClientBlock> createAnimationController(TrapDoorClientBlock base) {
		return TrapDoorAnimationController::new;
	}
}

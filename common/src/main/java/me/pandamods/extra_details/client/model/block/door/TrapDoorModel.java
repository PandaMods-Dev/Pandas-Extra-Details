package me.pandamods.extra_details.client.model.block.door;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.animation_controller.block.door.TrapDoorAnimationController;
import me.pandamods.extra_details.entity.block.TrapDoorClientBlock;
import me.pandamods.extra_details.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.extra_details.pandalib.utils.RenderUtils;
import me.pandamods.extra_details.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class TrapDoorModel implements MeshModel<TrapDoorClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(TrapDoorClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/door/trap_door.json");
	}

	@Override
	public @Nullable ResourceLocation getArmatureLocation(TrapDoorClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/armatures/block/door/trap_door.json");
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

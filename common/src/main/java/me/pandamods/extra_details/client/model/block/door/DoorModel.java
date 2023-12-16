package me.pandamods.extra_details.client.model.block.door;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.animation_controller.block.door.DoorAnimationController;
import me.pandamods.extra_details.entity.block.DoorClientBlock;
import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.pandalib.utils.RenderUtils;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class DoorModel implements MeshModel<DoorClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(DoorClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/door/door.json");
	}

	@Override
	public @Nullable ResourceLocation getArmatureLocation(DoorClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/armatures/block/door/door.json");
	}

	@Override
	public Map<String, ResourceLocation> getTextureLocations(DoorClientBlock base) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(base.getBlockState());
		ResourceLocation resourceLocation = textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			resourceLocation = new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		else
			resourceLocation = new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
		return Map.of("", resourceLocation);
	}

	@Override
	public AnimationControllerProvider<DoorClientBlock> createAnimationController(DoorClientBlock base) {
		return DoorAnimationController::new;
	}

	@Override
	public void setPropertiesOnCreation(DoorClientBlock base, Armature armature) {
		boolean mirror = base.getBlockState().getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
		armature.mirrorX(mirror, false, false);
		armature.mirrorY(false, mirror, false);
	}
}

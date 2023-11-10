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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import org.joml.Math;

import java.util.List;

public class DoorModel implements MeshModel<DoorClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(DoorClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/door/door.json");
	}

	@Override
	public ResourceLocation getTextureLocation(String textureName, DoorClientBlock base) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(base.getBlockState());
		ResourceLocation resourceLocation = textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
	}

	@Override
	public AnimationControllerProvider<DoorClientBlock> createAnimationController() {
		return DoorAnimationController::new;
	}

	@Override
	public void setupAnim(DoorClientBlock base, Armature armature, float deltaSeconds) {
		BlockState state = base.getBlockState();

		armature.getBone("door").ifPresent(bone -> {
			boolean isRightHinge = state.getValue(DoorBlock.HINGE).equals(DoorHingeSide.RIGHT);
			bone.setTranslation(isRightHinge ? (float) 0 : (float) 13 /16, 0, (float) -13 /16);
			bone.setRotation(0, (bone.getRotation().y * (isRightHinge ? -1 : 1)) + (isRightHinge ? 0 : (float) Math.PI), 0);
		});
	}
}

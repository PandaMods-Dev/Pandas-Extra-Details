package me.pandamods.extra_details.client.model.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.renderer.block.door.DoorRenderer;
import me.pandamods.extra_details.entity.block.DoorClientBlock;
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
	public void setupAnim(DoorClientBlock base, Armature armature, float deltaSeconds) {
		BlockState state = base.getBlockState();

		float speed = deltaSeconds / ExtraDetails.getConfig().door_animation_length;
		base.animTime = Math.clamp(0, 1, base.animTime + (state.getValue(DoorBlock.OPEN) ? speed : -speed));

		float animValue = state.getValue(DoorBlock.OPEN) ?
				DoorRenderer.doorAnimation.getValue(base.animTime) :
				1 - DoorRenderer.doorAnimation.getValue(1 - base.animTime);

		armature.getBone("door").ifPresent(bone -> {
			boolean isRightHinge = state.getValue(DoorBlock.HINGE).equals(DoorHingeSide.RIGHT);
			bone.setTranslation(isRightHinge ? (float) 0 : (float) 13 /16, 0, (float) -13 /16);
			bone.setRotation(0,
					Math.toRadians(Math.lerp(0, isRightHinge ? -90 : 90, animValue) + (isRightHinge ? 0 : 180)), 0);
		});
	}
}

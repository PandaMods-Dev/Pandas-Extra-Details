package me.pandamods.extra_details.client.model.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.renderer.block.door.DoorRenderer;
import me.pandamods.extra_details.entity.block.TrapDoorClientBlock;
import me.pandamods.pandalib.utils.RenderUtils;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import org.joml.Math;

import java.util.List;

public class TrapDoorModel implements MeshModel<TrapDoorClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(TrapDoorClientBlock entity) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "meshes/block/door/trap_door.json");
	}

	@Override
	public ResourceLocation getTextureLocation(String textureName, TrapDoorClientBlock entity) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(entity.getBlockState());
		ResourceLocation resourceLocation = textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
	}

	@Override
	public void setupAnim(TrapDoorClientBlock entity, Armature armature, float deltaSeconds) {
		BlockState state = entity.getBlockState();

		float speed = deltaSeconds / ExtraDetails.getConfig().trap_door_animation_length;
		entity.animTime = Math.clamp(0, 1, entity.animTime + (state.getValue(TrapDoorBlock.OPEN) ? speed : -speed));

		float animValue = state.getValue(TrapDoorBlock.OPEN) ?
				DoorRenderer.doorAnimation.getValue(entity.animTime) :
				1 - DoorRenderer.doorAnimation.getValue(1 - entity.animTime);

		armature.getBone("door").ifPresent(bone -> {
			boolean isTop = state.getValue(TrapDoorBlock.HALF).equals(Half.TOP);
			if (isTop)
				bone.localTransform.setTranslation(0, (float) 13 / 16, 0);
			bone.localTransform.setRotationXYZ(Math.toRadians(
					Math.lerp(0, isTop ? 90 : -90, animValue)), 0, 0);
		});
	}
}

package me.pandamods.extra_details.client.model.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.renderer.block.door.DoorRenderer;
import me.pandamods.extra_details.entity.block.FenceGateEntity;
import me.pandamods.extra_details.utils.RenderUtils;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;

import java.util.List;

public class FenceGateModel implements MeshModel<FenceGateEntity> {
	@Override
	public ResourceLocation getMeshLocation(FenceGateEntity entity) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "meshes/block/door/fence_gate.json");
	}

	@Override
	public ResourceLocation getTextureLocation(String textureName, FenceGateEntity entity) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(entity.getBlockState());
		ResourceLocation resourceLocation = textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
	}

	@Override
	public void setupAnim(FenceGateEntity entity, Armature armature, float deltaSeconds) {
		BlockState state = entity.getBlockState();

		float speed = deltaSeconds / ExtraDetails.getConfig().fence_gate_animation_length;
		entity.openingTime = Math.clamp(0, 1, entity.openingTime + (state.getValue(FenceGateBlock.OPEN) ? speed : -speed));

		float animValue = state.getValue(FenceGateBlock.OPEN) ?
				DoorRenderer.doorAnimation.getValue(entity.openingTime) :
				1 - DoorRenderer.doorAnimation.getValue(1 - entity.openingTime);

		armature.getBone("right.door").ifPresent(bone ->
				bone.localTransform.setRotationXYZ(0, Math.toRadians(Math.lerp(0, 90, animValue)), 0));
		armature.getBone("left.door").ifPresent(bone ->
				bone.localTransform.setRotationXYZ(0, Math.toRadians(Math.lerp(0, -90, animValue)), 0));
	}
}

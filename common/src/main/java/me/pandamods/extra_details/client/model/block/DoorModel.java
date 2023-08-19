package me.pandamods.extra_details.client.model.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.renderer.block.door.DoorRenderer;
import me.pandamods.extra_details.entity.block.DoorClientBlock;
import me.pandamods.extra_details.utils.RenderUtils;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.Half;
import org.joml.Math;

import java.util.List;

public class DoorModel implements MeshModel<DoorClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(DoorClientBlock entity) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "meshes/block/door/door.json");
	}

	@Override
	public ResourceLocation getTextureLocation(String textureName, DoorClientBlock entity) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(entity.getBlockState());
		ResourceLocation resourceLocation = textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
	}

	@Override
	public void setupAnim(DoorClientBlock entity, Armature armature, float deltaSeconds) {
		BlockState state = entity.getBlockState();

		float speed = deltaSeconds / ExtraDetails.getConfig().fence_gate_animation_length;
		entity.animTime = Math.clamp(0, 1, entity.animTime + (state.getValue(DoorBlock.OPEN) ? speed : -speed));

		float animValue = state.getValue(DoorBlock.OPEN) ?
				DoorRenderer.doorAnimation.getValue(entity.animTime) :
				1 - DoorRenderer.doorAnimation.getValue(1 - entity.animTime);

		armature.getBone("door").ifPresent(bone -> {
			boolean isRightHinge = state.getValue(DoorBlock.HINGE).equals(DoorHingeSide.RIGHT);
			bone.localTransform.setTranslation(isRightHinge ? (float) 0 : (float) 13 /16, 0, (float) -13 /16);
			bone.localTransform.setRotationXYZ(0,
					Math.toRadians(Math.lerp(0, isRightHinge ? -90 : 90, animValue) + (isRightHinge ? 0 : 180)), 0);
		});
	}
}

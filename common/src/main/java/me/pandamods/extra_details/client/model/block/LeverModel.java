package me.pandamods.extra_details.client.model.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.renderer.block.door.DoorRenderer;
import me.pandamods.extra_details.entity.block.LeverClientBlock;
import me.pandamods.pandalib.utils.RenderUtils;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;

import java.util.List;

public class LeverModel implements MeshModel<LeverClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(LeverClientBlock entity) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "meshes/block/redstone/lever.json");
	}

	@Override
	public ResourceLocation getTextureLocation(String textureName, LeverClientBlock entity) {
		return switch (textureName) {
			case "lever" -> getTextureResource(entity, true);
			default -> getTextureResource(entity, false);
		};
	}

	public ResourceLocation getTextureResource(LeverClientBlock animatable, boolean bl) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(animatable.getBlockState());
		ResourceLocation resourceLocation = bl && textures.size() > 1 ? textures.get(1) : textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
	}

	@Override
	public void setupAnim(LeverClientBlock entity, Armature armature, float deltaSeconds) {
		BlockState state = entity.getBlockState();

		float speed = deltaSeconds / ExtraDetails.getConfig().lever_animation_length;
		entity.animTime = Math.clamp(0, 1, entity.animTime + (state.getValue(LeverBlock.POWERED) ? speed : -speed));

		float animValue = state.getValue(LeverBlock.POWERED) ?
				DoorRenderer.doorAnimation.getValue(entity.animTime) :
				1 - DoorRenderer.doorAnimation.getValue(1 - entity.animTime);

		armature.getBone("handle").ifPresent(bone ->
				bone.localTransform.setRotationXYZ(Math.toRadians(Math.lerp(-45, 45, animValue)), 0, 0));
	}
}

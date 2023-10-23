package me.pandamods.extra_details.client.model.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.renderer.block.door.DoorRenderer;
import me.pandamods.extra_details.entity.block.FenceGateClientBlock;
import me.pandamods.pandalib.utils.RenderUtils;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;

import java.util.List;

public class FenceGateModel implements MeshModel<FenceGateClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(FenceGateClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/door/fence_gate.json");
	}

	@Override
	public ResourceLocation getTextureLocation(String textureName, FenceGateClientBlock base) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(base.getBlockState());
		ResourceLocation resourceLocation = textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
	}

	@Override
	public void setupAnim(FenceGateClientBlock base, Armature armature, float deltaSeconds) {
		BlockState state = base.getBlockState();

		float speed = deltaSeconds / ExtraDetails.getConfig().fence_gate_animation_length;
		base.animTime = Math.clamp(0, 1, base.animTime + (state.getValue(FenceGateBlock.OPEN) ? speed : -speed));

		float animValue = state.getValue(FenceGateBlock.OPEN) ?
				DoorRenderer.doorAnimation.getValue(base.animTime) :
				1 - DoorRenderer.doorAnimation.getValue(1 - base.animTime);

		armature.getBone("right.door").ifPresent(bone ->
				bone.setRotation(0, Math.toRadians(Math.lerp(0, 90, animValue)), 0));
		armature.getBone("left.door").ifPresent(bone ->
				bone.setRotation(0, Math.toRadians(Math.lerp(0, -90, animValue)), 0));
	}
}

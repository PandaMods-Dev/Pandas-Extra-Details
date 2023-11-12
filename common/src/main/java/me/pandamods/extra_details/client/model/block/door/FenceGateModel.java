package me.pandamods.extra_details.client.model.block.door;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.animation_controller.block.door.FenceDoorAnimationController;
import me.pandamods.extra_details.client.renderer.block.door.DoorRenderer;
import me.pandamods.extra_details.entity.block.FenceGateClientBlock;
import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.pandalib.utils.RenderUtils;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
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
	public AnimationControllerProvider<FenceGateClientBlock> createAnimationController() {
		return FenceDoorAnimationController::new;
	}

	@Override
	public void setupAnim(FenceGateClientBlock base, Armature armature, float deltaSeconds) {
		armature.getBone("right.door").ifPresent(bone ->
				bone.setRotation(bone.getRotation().x, -bone.getRotation().y, bone.getRotation().z));
		armature.getBone("left.door").ifPresent(bone ->
				bone.setRotation(bone.getRotation().x, -bone.getRotation().y, bone.getRotation().z));
	}
}

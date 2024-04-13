package me.pandamods.extra_details.client.model.block.door;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.animation_controller.block.door.FenceDoorAnimationController;
import me.pandamods.extra_details.entity.block.FenceGateClientBlock;
import me.pandamods.extra_details.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.extra_details.pandalib.utils.RenderUtils;
import me.pandamods.extra_details.pandalib.client.model.Armature;
import me.pandamods.extra_details.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

import java.util.List;
import java.util.Map;

public class FenceGateModel implements MeshModel<FenceGateClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(FenceGateClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/door/fence_gates/fence_gate.json");
	}

	@Override
	public @Nullable ResourceLocation getArmatureLocation(FenceGateClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/armatures/block/door/fence_gate.json");
	}

	@Override
	public Map<String, ResourceLocation> getTextureLocations(FenceGateClientBlock base) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(base.getBlockState());
		ResourceLocation resourceLocation = textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			resourceLocation = new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		else
			resourceLocation = new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
		return Map.of("", resourceLocation);
	}

	@Override
	public AnimationControllerProvider<FenceGateClientBlock> createAnimationController(FenceGateClientBlock base) {
		return FenceDoorAnimationController::new;
	}

	@Override
	public void setupAnim(FenceGateClientBlock base, Armature armature, float deltaSeconds) {
		armature.getBone("right.door").ifPresent(bone ->
				bone.localTransform(matrix -> {
					Quaternionf quaternion = matrix.getNormalizedRotation(new Quaternionf());
					Quaternionf newQuaternion = new Quaternionf(
							quaternion.x ,
							-quaternion.y,
							quaternion.z ,
							quaternion.w
					);
					newQuaternion.normalize();
					return matrix.rotation(newQuaternion);
				})
		);
		armature.getBone("left.door").ifPresent(bone ->
				bone.localTransform(matrix -> {
					Quaternionf quaternion = matrix.getNormalizedRotation(new Quaternionf());
					Quaternionf newQuaternion = new Quaternionf(
							quaternion.x ,
							-quaternion.y,
							quaternion.z ,
							quaternion.w
					);
					newQuaternion.normalize();
					return matrix.rotation(newQuaternion);
				})
		);
	}
}

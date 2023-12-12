package me.pandamods.extra_details.client.model.block.redstone;

import com.google.common.collect.ImmutableMap;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.animation_controller.block.redstone.LeverAnimationController;
import me.pandamods.extra_details.entity.block.LeverClientBlock;
import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.utils.RenderUtils;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import java.util.List;
import java.util.Map;

public class LeverModel implements MeshModel<LeverClientBlock> {
	static boolean DEBUG = false;

	@Override
	public ResourceLocation getMeshLocation(LeverClientBlock base) {
		if (DEBUG) return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/debug.json");
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/redstone/lever.json");
	}

	@Override
	public @Nullable ResourceLocation getArmatureLocation(LeverClientBlock base) {
		if (DEBUG) return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/armatures/debug.json");
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/armatures/block/redstone/lever.json");
	}

	@Override
	public Map<String, ResourceLocation> getTextureLocations(LeverClientBlock base) {
		return Map.of(
				"lever", getTextureResource(base, true),
				"cobblestone", getTextureResource(base, false)
		);
	}

	public ResourceLocation getTextureResource(LeverClientBlock animatable, boolean bl) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(animatable.getBlockState());
		ResourceLocation resourceLocation = bl && textures.size() > 1 ? textures.get(1) : textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
	}

	@Override
	public AnimationControllerProvider<LeverClientBlock> createAnimationController(LeverClientBlock base) {
		if (DEBUG) return null;
		return LeverAnimationController::new;
	}

	float debugAnimTime = 0;

	@Override
	public void setupAnim(LeverClientBlock base, Armature armature, float deltaSeconds) {
		if (DEBUG)
			armature.getBone("bone2").ifPresent(bone ->
					bone.localTransform(matrix -> matrix.setRotationXYZ((float) (Math.cos(debugAnimTime += deltaSeconds) * (Math.PI / 2)), 0, 0)));
	}
}

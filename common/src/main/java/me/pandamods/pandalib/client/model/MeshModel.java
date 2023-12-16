package me.pandamods.pandalib.client.model;

import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public interface MeshModel<T extends MeshAnimatable> {
	@Nullable
	default ResourceLocation getMeshLocation(T base) {
		return null;
	}
	@Nullable
	default ResourceLocation getArmatureLocation(T base) {
		return null;
	}
	default Map<String, ResourceLocation> getTextureLocations(T base) {
		return new HashMap<>();
	}
	@Nullable
	default AnimationControllerProvider<T> createAnimationController(T base) {
		return null;
	}

	default void setupAnim(T base, Armature armature, float deltaSeconds) {}

	default void setPropertiesOnCreation(T base, Armature armature) {}
}

package me.pandamods.pandalib.client.model;

import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public interface MeshModel<T extends MeshAnimatable> {
	ResourceLocation getMeshLocation(T base);
	ResourceLocation getTextureLocation(String textureName, T base);
	default AnimationControllerProvider<T> createAnimationController() {
		return null;
	}

	default void setupAnim(T base, Armature armature, float deltaSeconds) {}

	default void setPropertiesOnCreation(T base, Armature armature) {}
}

package me.pandamods.pandalib.client.model;

import me.pandamods.pandalib.entity.MeshAnimatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

@Environment(EnvType.CLIENT)
public interface MeshModel<T extends MeshAnimatable> {
	ResourceLocation getMeshLocation(T entity);
	ResourceLocation getTextureLocation(String textureName, T entity);

	default void setupAnim(T entity, Armature armature, float deltaSeconds) {}
}

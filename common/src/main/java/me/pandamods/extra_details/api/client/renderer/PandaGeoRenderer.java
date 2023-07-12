package me.pandamods.extra_details.api.client.renderer;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface PandaGeoRenderer<T> {
	ResourceLocation getModelResource(T entity);
	ResourceLocation getTextureResource(T entity);
}

package me.pandamods.pandalib.client;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface Model<T> {
	ResourceLocation modelLocation(T t);
	Map<String, ResourceLocation> textureLocation(T t);
}

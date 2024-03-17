package me.pandamods.pandalib.client;

import me.pandamods.pandalib.client.armature.Armature;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface Model<T> {
	ResourceLocation modelLocation(T t);
	ResourceLocation textureLocation(T t, String name);
}

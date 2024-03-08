package me.pandamods.pandalib.client.animation;

import me.pandamods.pandalib.client.armature.Armature;
import me.pandamods.pandalib.client.armature.IArmatureCache;
import net.minecraft.resources.ResourceLocation;

public interface AnimationController<T extends IAnimatableCache> {
	ResourceLocation armatureLocation(T t);

	void animate(T t, Armature armature, float partialTick);
}

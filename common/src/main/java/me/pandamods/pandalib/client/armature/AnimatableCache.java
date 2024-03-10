package me.pandamods.pandalib.client.armature;

import me.pandamods.pandalib.client.animation.AnimationController;
import me.pandamods.pandalib.client.animation.AnimationHandler;
import net.minecraft.resources.ResourceLocation;

public class AnimatableCache {
	public ResourceLocation resourceLocation = null;
	public Armature armature = null;
	public AnimationHandler<? extends AnimatableCache> animationHandler = null;
}

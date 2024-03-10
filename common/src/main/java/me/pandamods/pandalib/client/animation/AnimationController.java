package me.pandamods.pandalib.client.animation;

import me.pandamods.pandalib.client.armature.Armature;
import me.pandamods.pandalib.client.armature.IAnimatable;
import net.minecraft.resources.ResourceLocation;

public interface AnimationController<T extends IAnimatable> {
	ResourceLocation armatureLocation(T t);

	AnimationState<T> registerAnimations();
	void mathAnimate(T t, Armature armature, float partialTick);

	default AnimationState<T> animate(String modId, String path, PlayType playType) {
		return animate(new ResourceLocation(modId, path), playType);
	}

	default AnimationState<T> animate(String modId, String path) {
		return animate(modId, path, PlayType.DEFAULT);
	}

	default AnimationState<T> animateLoop(String modId, String path) {
		return animate(modId, path, PlayType.LOOP);
	}

	default AnimationState<T> animate(ResourceLocation resourceLocation, PlayType playType) {
		return AnimationState.of(resourceLocation, playType);
	}

	default AnimationState<T> animate(ResourceLocation resourceLocation) {
		return animate(resourceLocation, PlayType.DEFAULT);
	}
}

package me.pandamods.pandalib.client.animation;

import me.pandamods.pandalib.client.armature.Armature;
import me.pandamods.pandalib.client.armature.IAnimatable;

import java.util.Objects;

public class AnimationHandler<T extends IAnimatable> {
	private final T t;

	private final AnimationState<T> rootAnimationState;
	private AnimationState<T> animationState;

	private float time;

	public AnimationHandler(T t, AnimationController<T> controller) {
		this.t = t;
		this.rootAnimationState = controller.registerAnimations();
		this.animationState = this.rootAnimationState.start(time);
	}

	public void update(Armature armature, float time) {
		this.time = time;
		this.animationState.update(this.t, armature,this, time);
	}

	public void setAnimationState(AnimationState<T> animationState) {
		if (animationState == null) {
			this.animationState = this.rootAnimationState.start(time);
		} else if (!Objects.equals(this.animationState, animationState)) {
			this.animationState = animationState.start(time);
		}
	}
}
package me.pandamods.pandalib.client.animation;

import me.pandamods.pandalib.client.animation.states.State;

public class AnimatableInstance {
	private final Animatable animatable;
	private State state;

	public AnimatableInstance(Animatable animatable) {
		this.animatable = animatable;
	}

	public float getTick(float partialTick) {
		return this.animatable.getTick();
	}

	public float getTick() {
		return getTick(0);
	}

	public void setState(State state) {
		this.state = state;
		this.state.start(this.getTick());
	}

	public State getState() {
		return state;
	}
}

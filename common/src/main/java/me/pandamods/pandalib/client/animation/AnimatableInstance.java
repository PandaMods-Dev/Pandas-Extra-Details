/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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

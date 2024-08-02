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

package me.pandamods.pandalib.client.animation.states;

import me.pandamods.pandalib.resource.model.Node;
import me.pandamods.pandalib.utils.MathUtils;
import org.joml.Matrix4f;

public class TransitionState extends State {
	private final State previousState;
	private final State nextState;
	private final float duration;

	public TransitionState(State previousState, State nextState, float duration) {
		this.previousState = previousState;
		this.nextState = nextState;
		this.duration = duration;

		nextState(this::isFinished, nextState);
	}

	@Override
	public Matrix4f getBoneTransform(Node node) {
		float alpha = this.getTime() / getDuration();
		Matrix4f previousMatrix = previousState.getBoneTransform(node);
    	Matrix4f nextMatrix = nextState.getBoneTransform(node);
    	return MathUtils.lerpMatrix(previousMatrix, nextMatrix, alpha);
	}

	@Override
	public void start(float startTick) {
		super.start(startTick);
		this.nextState.start(startTick);
	}

	@Override
	public float getDuration() {
		return this.duration;
	}
}

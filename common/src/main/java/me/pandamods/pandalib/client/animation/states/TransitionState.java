package me.pandamods.pandalib.client.animation.states;

import me.pandamods.pandalib.resource.Model;
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
	public Matrix4f getBoneTransform(Model.Bone bone) {
		float alpha = this.getTime() / getDuration();
		Matrix4f previousMatrix = previousState.getBoneTransform(bone);
    	Matrix4f nextMatrix = nextState.getBoneTransform(bone);
    	return MathUtils.lerp(previousMatrix, nextMatrix, alpha);
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

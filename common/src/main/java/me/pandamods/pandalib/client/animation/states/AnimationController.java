package me.pandamods.pandalib.client.animation.states;

import me.pandamods.pandalib.client.animation.Animatable;
import me.pandamods.pandalib.client.animation.AnimatableInstance;
import me.pandamods.pandalib.resource.Mesh;

public interface AnimationController<T extends Animatable> {
	State registerStates(T t);

	default void animate(T t, Mesh mesh, float partialTick) {
		AnimatableInstance instance = t.getAnimatableInstance();
		State state = instance.getState();
		if (state == null)
			instance.setState(state = registerStates(t));

		try {
			if (state == null)
				throw new NullPointerException("Animation state is NULL");
		} catch (NullPointerException e) {
			throw new RuntimeException(e);
		}

		state.updateTime(instance, partialTick);
		state.checkStateSwitch(instance);
		mesh.forEachBone((s, bone) -> processBoneMatrix(bone, instance));
	}

	default void processBoneMatrix(Mesh.Bone bone, AnimatableInstance instance) {
		bone.getLocalMatrix().set(instance.getState().getBoneMatrix(bone));
	}
}

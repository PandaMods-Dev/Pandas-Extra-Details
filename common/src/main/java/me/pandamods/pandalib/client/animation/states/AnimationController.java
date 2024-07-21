package me.pandamods.pandalib.client.animation.states;

import me.pandamods.pandalib.client.animation.Animatable;
import me.pandamods.pandalib.client.animation.AnimatableInstance;
import me.pandamods.pandalib.resource.Model;

public interface AnimationController<T extends Animatable> {
	State registerStates(T t);

	default void animate(T t, Model model, float partialTick) {
		AnimatableInstance instance = t.getAnimatableInstance();
		State state = instance.getState();
		if (state == null)
			instance.setState(state = registerStates(t));

		if (state == null)
			throw new NullPointerException("Animation state is NULL");

		state.updateTime(instance, partialTick);
		state.checkStateSwitch(instance);
		for (Model.Bone bone : model.getBones()) {
			processBoneTransform(bone, instance);
		}
	}

	default void processBoneTransform(Model.Bone bone, AnimatableInstance instance) {
		bone.getLocalTransform().set(instance.getState().getBoneTransform(bone));
	}
}

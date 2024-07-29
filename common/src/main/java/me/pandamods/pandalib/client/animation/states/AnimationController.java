package me.pandamods.pandalib.client.animation.states;

import me.pandamods.pandalib.client.animation.Animatable;
import me.pandamods.pandalib.client.animation.AnimatableInstance;
import me.pandamods.pandalib.resource.model.Model;
import me.pandamods.pandalib.resource.model.Node;
import me.pandamods.pandalib.utils.PrintUtils;
import org.joml.Vector3f;

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
		for (Node node : model.getNodes()) {
			processBoneTransform(node, instance);
		}
	}

	default void processBoneTransform(Node node, AnimatableInstance instance) {
//		node.setLocalTransform(instance.getState().getBoneTransform(node));
		node.getRelativeTransform().set(instance.getState().getBoneTransform(node));
	}
}

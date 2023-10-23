package me.pandamods.pandalib.client.animation_controller;

import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.entity.MeshAnimatable;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AnimationController<T extends MeshAnimatable> {
	private final T base;
	private final ApplyMethod applyMethod;
	private final List<AnimationController<T>> subControllers = new ArrayList<>();

	private Animation currentAnimation;

	private float animationTime = 0;

	public AnimationController(T base) {
		this(base, ApplyMethod.REPLACE_ALL);
	}

	public AnimationController(T base, ApplyMethod applyMethod) {
		this.base = base;
		this.applyMethod = applyMethod;
	}

	public final Armature getArmature() {
		return base.getCache().armature;
	}

	public final void updateAnimations(float deltaSeconds) {
		Animation animation = controller(base, getArmature(), deltaSeconds);
		if (animation != this.currentAnimation) {
			this.currentAnimation = animation;
			this.animationTime = 0;
		}

		if (this.currentAnimation != null) {
			Set<String> boneNames = this.applyMethod.equals(ApplyMethod.REPLACE_ALL) ?
					this.getArmature().getBones().keySet() :
					this.currentAnimation.rawAnimation().bones().keySet();
			for (String boneName : boneNames) {
				getArmature().getBone(boneName).ifPresent(bone -> {
					Matrix4f transform = this.currentAnimation.getBoneTransform(boneName, this.animationTime);
					if (this.applyMethod.equals(ApplyMethod.ADD)) {
						transform.mul(bone.getLocalTransform());
					}
					bone.setLocalTransform(transform);
				});
			}

			for (AnimationController<T> subController : this.subControllers) {
				subController.updateAnimations(deltaSeconds);
			}

			this.animationTime += deltaSeconds;
			if (this.currentAnimation.playType().equals(PlayType.LOOP) && this.animationTime >= this.currentAnimation.rawAnimation().animation_length()) {
				this.animationTime -= this.currentAnimation.rawAnimation().animation_length();
			}
		}
	}

	public abstract Animation controller(T base, Armature armature, float deltaSeconds);

	protected final void registerSubController(AnimationControllerProvider<T> newSubAnimationController) {
		this.subControllers.add(newSubAnimationController.create(this.base));
	}

	public enum ApplyMethod {
		/**
		 * Replaces all bone's transform's
		 */
		REPLACE_ALL,
		/**
		 * Only replaces the transform's of bone's affected by the animation
		 */
		REPLACE_CHANGED,
		/**
		 * Adds the transform of bone's affected by the animation
		 */
		ADD
	}
}

package me.pandamods.pandalib.client.animation_controller;

import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.entity.MeshAnimatable;
import me.pandamods.pandalib.utils.MatrixUtils;
import org.joml.Math;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AnimationController<T extends MeshAnimatable> {
	private final T base;
	private final ApplyMethod applyMethod;
	private final List<AnimationController<T>> subControllers = new ArrayList<>();

	private Animation previousAnimation;
	private Animation currentAnimation;

	private float previousAnimationTime = 0;
	private float animationTime = 0;
	private float transitionTime = 0;

	private float transitionLength = 0;
	private float animationSpeed = 1;
	private boolean skipAnimation = false;

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
			this.previousAnimation = this.currentAnimation;
			this.previousAnimationTime = this.animationTime;

			this.currentAnimation = animation;
			this.animationTime = 0;
			if (this.previousAnimation != null)
				this.transitionTime = 0;
			else {
				this.transitionTime = 1;
			}
		}

		if (this.currentAnimation != null) {
			if (this.skipAnimation) {
				this.animationTime = this.currentAnimation.rawAnimation().animation_length();
				this.skipAnimation = false;
			}

			Set<String> boneNames = this.applyMethod.equals(ApplyMethod.REPLACE_ALL) ?
					this.getArmature().getBones().keySet() :
					this.currentAnimation.rawAnimation().bones().keySet();
			for (String boneName : boneNames) {
				getArmature().getBone(boneName).ifPresent(bone -> {
					Matrix4f transform = this.currentAnimation.getBoneTransform(boneName, this.animationTime);
					if (this.applyMethod.equals(ApplyMethod.ADD)) {
						transform.mul(bone.localTransform());
					}

					if (this.previousAnimation != null) {
						Matrix4f previousTransform = this.previousAnimation.getBoneTransform(boneName, this.previousAnimationTime);
						transform = MatrixUtils.lerpMatrix(previousTransform, transform, this.transitionTime);
					}

					Matrix4f finalTransform = transform;
					bone.localTransform(matrix4f -> finalTransform);
				});
			}

			for (AnimationController<T> subController : this.subControllers) {
				subController.updateAnimations(deltaSeconds);
			}

			this.transitionTime = Math.min(this.transitionTime + (deltaSeconds / this.transitionLength), 1);
			if (transitionTime >= 1)
				this.animationTime += deltaSeconds * this.animationSpeed;
			if (this.currentAnimation.playType().equals(PlayType.LOOP) && this.animationTime >= this.currentAnimation.rawAnimation().animation_length()) {
				this.animationTime -= this.currentAnimation.rawAnimation().animation_length();
			}
		}
	}

	public abstract Animation controller(T base, Armature armature, float deltaSeconds);

	protected final void registerSubController(AnimationControllerProvider<T> newSubAnimationController) {
		this.subControllers.add(newSubAnimationController.create(this.base));
	}

	protected final void skipAnimation() {
		this.skipAnimation = true;
	}

	public void setAnimationTime(float animationTime) {
		this.animationTime = animationTime;
	}

	public void setTransitionLength(float transitionLength) {
		this.transitionLength = transitionLength;
	}

	public void setAnimationSpeed(float speed) {
		this.animationSpeed = speed;
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

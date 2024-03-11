package me.pandamods.pandalib.client.animation;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.client.armature.Armature;
import me.pandamods.pandalib.client.armature.IAnimatable;
import me.pandamods.pandalib.resource.AnimationData;
import me.pandamods.pandalib.utils.TransformUtils;
import net.minecraft.resources.ResourceLocation;
import org.joml.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnimationState<T extends IAnimatable> {
	private final List<Branch<T>> branches = new ArrayList<>();
	private final AnimationData animation;
	private final PlayType playType;

	private float time = 0;
	private float startTime = 0;

	private Branch<T> nextBranch = null;
	private float transitionTime = 0;
	private float transitionStartTime = 0;

	private AnimationState(ResourceLocation resourceLocation, PlayType playType) {
		this.animation = ExtraDetails.RESOURCES.animations.get(resourceLocation);
		this.playType = playType;
	}

	public static <T extends IAnimatable> AnimationState<T> of(ResourceLocation resourceLocation, PlayType playType) {
		return new AnimationState<T>(resourceLocation, playType);
	}

	public void update(T t, Armature armature, AnimationHandler<T> handler, float time) {
		if (nextBranch == null) {
			for (Branch<T> branch : branches) {
				if (branch.branchCondition.condition(t, this)) {
					transitionStartTime = time;
					nextBranch = branch;
					break;
				}
			}
		}
		updateTime(time);

		Set<String> boneNames = armature.getBones().keySet();
		for (String boneName : boneNames) {
			armature.getBone(boneName).ifPresent(bone -> {
				Matrix4f transform = getBoneTransform(boneName, this.time);
				if (nextBranch != null) TransformUtils.lerp(transform, nextBranch.state.getBoneTransform(boneName, 0), this.transitionTime);
				bone.localTransform.set(transform);
			});
		}

		if (nextBranch != null && transitionTime >= 1) {
			handler.setAnimationState(nextBranch.state);
		}
	}

	private void updateTime(float time) {
		if (nextBranch == null) {
			if (playType.equals(PlayType.LOOP) && isFinished()) {
				this.startTime += animation.animation_length();
			}

			this.time = time - this.startTime;
		} else {
			this.transitionTime = (time - this.transitionStartTime) / nextBranch.transitionTime;
		}
	}

	public Matrix4f getBoneTransform(String boneName, float time) {
		AnimationData.Bone bone = animation.bones().get(boneName);

		if (bone == null) {
			return new Matrix4f().identity();
		}

		Vector3fc position = interpolateKeyframes(bone.position(), time, new Vector3f(0));
		Quaternionfc rotation = interpolateKeyframes(bone.rotation(), time, new Quaternionf().identity());
		Vector3fc scale = interpolateKeyframes(bone.scale(), time, new Vector3f(1));

		Matrix4f translationMatrix = new Matrix4f().translation(position);
		Matrix4f rotationMatrix = new Matrix4f().rotation(rotation);
		Matrix4f scaleMatrix = new Matrix4f().scaling(scale);

		return new Matrix4f(translationMatrix).mul(rotationMatrix).mul(scaleMatrix);
	}

	public void registerBranch(AnimationState<T> state) {
		registerBranch(state, 1);
	}

	public void registerBranch(AnimationState<T> state, BranchCondition<T> condition) {
		registerBranch(state, 1, condition);
	}

	public void registerBranch(AnimationState<T> state, float transitionTime) {
		registerBranch(state, transitionTime, (t, state1) -> state1.isFinished());
	}

	public void registerBranch(AnimationState<T> state, float transitionTime, BranchCondition<T> condition) {
		this.branches.add(new Branch<>(state, transitionTime, condition));
	}

	public AnimationState<T> start(float startTime) {
		this.startTime = startTime;
		this.time = 0;
		this.transitionStartTime = startTime;
		this.transitionTime = 0;
		this.nextBranch = null;
		return this;
	}

	public boolean isFinished() {
		return this.time >= this.animation.animation_length();
	}

	private static Vector3fc interpolateKeyframes(Map<Float, Vector3fc> keyframes, float time, Vector3fc defaultVector) {
		if (keyframes.isEmpty()) {
			return defaultVector;
		}

		Map.Entry<Float, Vector3fc> firstEntry = keyframes.entrySet().iterator().next();

		if (time <= firstEntry.getKey()) {
			return firstEntry.getValue();
		}

		float prevTime = firstEntry.getKey();
		Vector3fc prevValue = firstEntry.getValue();

		for (Map.Entry<Float, Vector3fc> entry : keyframes.entrySet()) {
			float currentTime = entry.getKey();
			Vector3fc currentValue = entry.getValue();

			if (currentTime >= time) {
				float alpha = (time - prevTime) / (currentTime - prevTime);
				return prevValue.lerp(currentValue, alpha, new Vector3f());
			}

			prevTime = currentTime;
			prevValue = currentValue;
		}

		return prevValue;
	}

	private static Quaternionfc interpolateKeyframes(Map<Float, Quaternionfc> keyframes, float time, Quaternionfc defaultQuaternion) {
		if (keyframes.isEmpty()) {
			return defaultQuaternion;
		}

		Map.Entry<Float, Quaternionfc> firstEntry = keyframes.entrySet().iterator().next();

		if (time <= firstEntry.getKey()) {
			return firstEntry.getValue();
		}

		float prevTime = firstEntry.getKey();
		Quaternionfc prevValue = firstEntry.getValue();

		for (Map.Entry<Float, Quaternionfc> entry : keyframes.entrySet()) {
			float currentTime = entry.getKey();
			Quaternionfc currentValue = entry.getValue();

			if (currentTime >= time) {
				float alpha = (time - prevTime) / (currentTime - prevTime);
				return prevValue.slerp(currentValue, alpha, new Quaternionf());
			}

			prevTime = currentTime;
			prevValue = currentValue;
		}

		return prevValue;
	}

	public interface BranchCondition<T extends IAnimatable> {
		boolean condition(T t, AnimationState<T> state);
	}

	public record Branch<T extends IAnimatable>(AnimationState<T> state, float transitionTime, BranchCondition<T> branchCondition) {}
}

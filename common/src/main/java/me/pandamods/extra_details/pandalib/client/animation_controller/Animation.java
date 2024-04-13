package me.pandamods.extra_details.pandalib.client.animation_controller;

import me.pandamods.extra_details.pandalib.PandaLib;
import me.pandamods.extra_details.pandalib.resources.AnimationRecord;
import me.pandamods.extra_details.pandalib.resources.Resources;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;

public record Animation(AnimationRecord rawAnimation, PlayType playType) {
	public static Animation of(ResourceLocation location) {
		return of(location, PlayType.PLAY);
	}

	public static Animation of(ResourceLocation location, PlayType playType) {
		AnimationRecord rawAnimation = Resources.ANIMATIONS.get(location);
		if (rawAnimation != null)
			return new Animation(Resources.ANIMATIONS.get(location), playType);
		else {
			PandaLib.LOGGER.error("Cant find animation at " + location.toString());
		}
		return null;
	}

	public Matrix4f getBoneTransform(String boneName, float time) {
		AnimationRecord.Bone bone = rawAnimation.bones().get(boneName);

		if (bone == null) {
			return new Matrix4f().identity();
		}

		Vector3f position = interpolateKeyframes(bone.position(), time, new Vector3f(0));
		Quaternionf rotation = interpolateKeyframes(bone.rotation(), time, new Quaternionf().identity());
		Vector3f scale = interpolateKeyframes(bone.scale(), time, new Vector3f(1));

		Matrix4f translationMatrix = new Matrix4f().translation(position);
		Matrix4f rotationMatrix = new Matrix4f().rotation(rotation);
		Matrix4f scaleMatrix = new Matrix4f().scaling(scale);

		return new Matrix4f(translationMatrix).mul(rotationMatrix).mul(scaleMatrix);
	}

	private static Vector3f interpolateKeyframes(Map<Float, Vector3f> keyframes, float time, Vector3f defaultVector) {
		if (keyframes.isEmpty()) {
			return defaultVector;
		}

		Map.Entry<Float, Vector3f> firstEntry = keyframes.entrySet().iterator().next();

		if (time <= firstEntry.getKey()) {
			return firstEntry.getValue();
		}

		float prevTime = firstEntry.getKey();
		Vector3f prevValue = firstEntry.getValue();

		for (Map.Entry<Float, Vector3f> entry : keyframes.entrySet()) {
			float currentTime = entry.getKey();
			Vector3f currentValue = entry.getValue();

			if (currentTime >= time) {
				float alpha = (time - prevTime) / (currentTime - prevTime);
				return prevValue.lerp(currentValue, alpha, new Vector3f());
			}

			prevTime = currentTime;
			prevValue = currentValue;
		}

		return prevValue;
	}

	private static Quaternionf interpolateKeyframes(Map<Float, Quaternionf> keyframes, float time, Quaternionf defaultQuaternion) {
		if (keyframes.isEmpty()) {
			return defaultQuaternion;
		}

		Map.Entry<Float, Quaternionf> firstEntry = keyframes.entrySet().iterator().next();

		if (time <= firstEntry.getKey()) {
			return firstEntry.getValue();
		}

		float prevTime = firstEntry.getKey();
		Quaternionf prevValue = firstEntry.getValue();

		for (Map.Entry<Float, Quaternionf> entry : keyframes.entrySet()) {
			float currentTime = entry.getKey();
			Quaternionf currentValue = entry.getValue();

			if (currentTime >= time) {
				float alpha = (time - prevTime) / (currentTime - prevTime);
				return prevValue.slerp(currentValue, alpha, new Quaternionf());
			}

			prevTime = currentTime;
			prevValue = currentValue;
		}

		return prevValue;
	}
}

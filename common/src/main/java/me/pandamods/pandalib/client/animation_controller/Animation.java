package me.pandamods.pandalib.client.animation_controller;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.resources.RawAnimation;
import me.pandamods.pandalib.resources.Resources;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;

public record Animation(RawAnimation rawAnimation, PlayType playType) {
	public static Animation of(ResourceLocation location) {
		return of(location, PlayType.PLAY);
	}

	public static Animation of(ResourceLocation location, PlayType playType) {
		RawAnimation rawAnimation = Resources.ANIMATIONS.get(location);
		if (rawAnimation != null)
			return new Animation(Resources.ANIMATIONS.get(location), playType);
		else {
			PandaLib.LOGGER.error("Cant find animation at " + location.toString());
		}
		return null;
	}

	public Matrix4f getBoneTransform(String boneName, float time) {
		RawAnimation.Bone bone = rawAnimation.bones().get(boneName);

		if (bone == null) {
			return new Matrix4f().identity();
		}

		Vector3f position = interpolateKeyframes(bone.position(), time, new Vector3f());
		Vector3f rotation = interpolateKeyframes(bone.rotation(), time, new Vector3f());
		Vector3f scale = interpolateKeyframes(bone.scale(), time, new Vector3f(1));

		Quaternionf rotationQuaternion = new Quaternionf().identity();
		rotationQuaternion.rotationXYZ(rotation.x(), rotation.y(), rotation.z());

		Matrix4f translationMatrix = new Matrix4f().translation(position);
		Matrix4f rotationMatrix = new Matrix4f().rotation(rotationQuaternion);
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
				return lerp(prevValue, currentValue, alpha);
			}

			prevTime = currentTime;
			prevValue = currentValue;
		}

		return prevValue;
	}

	private static Vector3f lerp(Vector3f a, Vector3f b, float alpha) {
		return new Vector3f(
				a.x() + alpha * (b.x() - a.x()),
				a.y() + alpha * (b.y() - a.y()),
				a.z() + alpha * (b.z() - a.z())
		);
	}
}

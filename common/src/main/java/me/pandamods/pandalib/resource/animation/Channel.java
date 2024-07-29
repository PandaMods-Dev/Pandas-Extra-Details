package me.pandamods.pandalib.resource.animation;

import org.joml.*;

import java.util.List;

public record Channel(String name, List<Key<Vector3fc>> positionKeys, List<Key<Quaternionfc>> rotationKeys, List<Key<Vector3fc>> scalingKeys) {
	public Vector3fc interpolatePosition(float time) {
		if (positionKeys.isEmpty()) return new Vector3f();

		List<Key<Vector3fc>> keys = positionKeys();
		for (int i = 0; i < keys.size() - 1; i++) {
			Key<Vector3fc> key1 = keys.get(i);
			Key<Vector3fc> key2 = keys.get(i+1);

			if (key1.time() <= time && key2.time() >= time) {
				float keyDuration = key2.time() - key1.time();
				float interpolatedTime = (time - key1.time()) / keyDuration;
				return key1.value().lerp(key2.value(), interpolatedTime, new Vector3f());
			}
		}
		return keys.get(keys.size() - 1).value();
	}

	public Quaternionfc interpolateRotation(float time) {
		if (rotationKeys.isEmpty()) return new Quaternionf();

		List<Key<Quaternionfc>> keys = rotationKeys();
		for (int i = 0; i < keys.size() - 1; i++) {
			Key<Quaternionfc> key1 = keys.get(i);
			Key<Quaternionfc> key2 = keys.get(i+1);

			if (key1.time() <= time && key2.time() >= time) {
				float keyDuration = key2.time() - key1.time();
				float interpolatedTime = (time - key1.time()) / keyDuration;
				return key1.value().nlerp(key2.value(), interpolatedTime, new Quaternionf());
			}
		}
		return keys.get(keys.size() - 1).value();
	}

	public Vector3fc interpolateScale(float time) {
		if (scalingKeys.isEmpty()) return new Vector3f(1);

		List<Key<Vector3fc>> keys = scalingKeys();
		for (int i = 0; i < keys.size() - 1; i++) {
			Key<Vector3fc> key1 = keys.get(i);
			Key<Vector3fc> key2 = keys.get(i+1);

			if (key1.time() <= time && key2.time() >= time) {
				float keyDuration = key2.time() - key1.time();
				float interpolatedTime = (time - key1.time()) / keyDuration;
				return key1.value().lerp(key2.value(), interpolatedTime, new Vector3f());
			}
		}
		return keys.get(keys.size() - 1).value();
	}

	public Matrix4f getMatrix(float time, Matrix4f dist) {
		dist.identity();
		dist.translate(this.interpolatePosition(time));
		dist.rotate(this.interpolateRotation(time));
		dist.scale(this.interpolateScale(time));
		return dist;
	}

	public Matrix4f getMatrix(float time) {
		return getMatrix(time, new Matrix4f());
	}

	public record Key<T>(float time, T value) { }
}
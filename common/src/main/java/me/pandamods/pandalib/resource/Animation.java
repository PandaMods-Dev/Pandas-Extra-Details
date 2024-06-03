package me.pandamods.pandalib.resource;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.joml.*;
import org.joml.Math;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Animation {
	private float duration;
	private final List<Channel> channels = new ObjectArrayList<>();

	public Animation() {}

	public Animation(AIAnimation animation) {
		set(animation);
	}

	public Animation set(AIAnimation animation) {
		channels.clear();
		for (int i = 0; i < animation.mNumChannels(); i++) {
			AINodeAnim node = AINodeAnim.create(animation.mChannels().get(i));
			List<Key<Vector3fc>> positionKeys = new ArrayList<>();
			List<Key<Quaternionfc>> rotationKeys = new ArrayList<>();
			List<Key<Vector3fc>> scalingKeys = new ArrayList<>();

			for (AIVectorKey key : node.mPositionKeys()) {
				AIVector3D vector = key.mValue();
				positionKeys.add(new Key<>((float) (key.mTime() / animation.mTicksPerSecond()),
						new Vector3f(vector.x(), vector.y(), vector.z())));
			}

			for (AIQuatKey key : node.mRotationKeys()) {
				AIQuaternion quaternion = key.mValue();
				rotationKeys.add(new Key<>((float) (key.mTime() / animation.mTicksPerSecond()),
						new Quaternionf(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w())));
			}

			for (AIVectorKey key : node.mScalingKeys()) {
				AIVector3D vector = key.mValue();
				scalingKeys.add(new Key<>((float) (key.mTime() / animation.mTicksPerSecond()),
						new Vector3f(vector.x(), vector.y(), vector.z())));
			}

			channels.add(new Channel(node.mNodeName().dataString(), positionKeys, rotationKeys, scalingKeys));
		}

		this.duration = (float) (animation.mDuration() / animation.mTicksPerSecond());
		return this;
	}

	public float getDuration() {
		return duration;
	}

	public Collection<Channel> getChannels() {
		return channels;
	}

	public Channel getChannel(String name) {
		return channels.stream()
    			.filter(channel -> channel.name.equals(name))
    			.findFirst()
    			.orElse(null);
	}

	public record Channel(String name, List<Key<Vector3fc>> positionKeys, List<Key<Quaternionfc>> rotationKeys, List<Key<Vector3fc>> scalingKeys) {
		public Vector3fc interpolatePosition(float time) {
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
			List<Key<Quaternionfc>> keys = rotationKeys();
			for (int i = 0; i < keys.size() - 1; i++) {
				Key<Quaternionfc> key1 = keys.get(i);
				Key<Quaternionfc> key2 = keys.get(i+1);

				if (key1.time() <= time && key2.time() >= time) {
					float keyDuration = key2.time() - key1.time();
					float interpolatedTime = (time - key1.time()) / keyDuration;
					return key1.value().slerp(key2.value(), interpolatedTime, new Quaternionf());
				}
			}
			return keys.get(keys.size() - 1).value();
		}

		public Vector3fc interpolateScale(float time) {
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

		public Matrix4f getMatrix(float time) {
			Matrix4f matrix = new Matrix4f();
			matrix.translate(this.interpolatePosition(time));
			matrix.rotate(this.interpolateRotation(time));
			matrix.scale(this.interpolateScale(time));
			return matrix;
		}
	}

	public record Key<T>(float time, T value) { }

	public enum ApplyMethod {
		SET,
		ADD
	}
}

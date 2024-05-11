package me.pandamods.pandalib.resource;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Animation {
	private final double duration;
	private final Channel[] channels;

	@SuppressWarnings("unchecked")
	public Animation(AIAnimation animation) {
		List<Channel> channels = new ArrayList<>();

		for (int i = 0; i < animation.mNumChannels(); i++) {
			AINodeAnim node = AINodeAnim.create(animation.mChannels().get(i));
			List<Key<Vector3fc>> positionKeys = new ArrayList<>();
			List<Key<Quaternionfc>> rotationKeys = new ArrayList<>();
			List<Key<Vector3fc>> scalingKeys = new ArrayList<>();

			for (AIVectorKey positionKey : node.mPositionKeys()) {
				AIVector3D vector = positionKey.mValue();
				positionKeys.add(new Key<>(positionKey.mTime(), new Vector3f(vector.x(), vector.y(), vector.z())));
			}

			for (AIQuatKey rotationKey : node.mRotationKeys()) {
				AIQuaternion quaternion = rotationKey.mValue();
				rotationKeys.add(new Key<>(rotationKey.mTime(), new Quaternionf(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w())));
			}

			for (AIVectorKey scalingKey : node.mScalingKeys()) {
				AIVector3D vector = scalingKey.mValue();
				scalingKeys.add(new Key<>(scalingKey.mTime(), new Vector3f(vector.x(), vector.y(), vector.z())));
			}

			channels.add(new Channel(node.mNodeName().dataString(),
					positionKeys.toArray(new Key[0]), rotationKeys.toArray(new Key[0]), scalingKeys.toArray(new Key[0])));
		}

		this.duration = animation.mDuration() / animation.mTicksPerSecond();
		this.channels = channels.toArray(new Channel[0]);
	}

	public record Channel(String name, Key<Vector3fc>[] positionKeys, Key<Vector3fc>[] rotationKeys, Key<Vector3fc>[] scalingKeys) { }
	public record Key<T>(double time, T value) { }
}

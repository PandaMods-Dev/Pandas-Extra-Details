/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.resource.loader;

import me.pandamods.pandalib.resource.animation.Animation;
import me.pandamods.pandalib.resource.animation.Channel;
import me.pandamods.pandalib.resource.model.Model;
import me.pandamods.pandalib.resource.model.Node;
import me.pandamods.pandalib.utils.PrintUtils;
import org.joml.*;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.List;

public class AnimationLoader {
	public static Animation loadAnimation(Animation animation, AIAnimation aiAnimation) {
		List<Channel> channels = new ArrayList<>();
		for (int i = 0; i < aiAnimation.mNumChannels(); i++) {
			AINodeAnim aiNodeAnim = AINodeAnim.create(aiAnimation.mChannels().get(i));

			List<Channel.Key<Vector3fc>> positionKeys = new ArrayList<>();
			List<Channel.Key<Quaternionfc>> rotationKeys = new ArrayList<>();
			List<Channel.Key<Vector3fc>> scalingKeys = new ArrayList<>();

			//Todo move the position key processing to a separate method
			for (AIVectorKey key : aiNodeAnim.mPositionKeys()) {
				AIVector3D vector = key.mValue();
				positionKeys.add(new Channel.Key<>((float) (key.mTime() / aiAnimation.mTicksPerSecond()),
						new Vector3f(vector.x(), vector.y(), vector.z())));
			}

			//Todo move the rotation key processing to a separate method
			for (AIQuatKey key : aiNodeAnim.mRotationKeys()) {
				AIQuaternion quaternion = key.mValue();
				rotationKeys.add(new Channel.Key<>((float) (key.mTime() / aiAnimation.mTicksPerSecond()),
						new Quaternionf(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w())));
			}

			//Todo move the scaling key processing to a separate method
			for (AIVectorKey key : aiNodeAnim.mScalingKeys()) {
				AIVector3D vector = key.mValue();
				scalingKeys.add(new Channel.Key<>((float) (key.mTime() / aiAnimation.mTicksPerSecond()),
						new Vector3f(vector.x(), vector.y(), vector.z())));
			}

			channels.add(new Channel(aiNodeAnim.mNodeName().dataString(), positionKeys, rotationKeys, scalingKeys));
		}

		float duration = (float) (aiAnimation.mDuration() / aiAnimation.mTicksPerSecond());
		return animation.set(channels, duration);
	}
}

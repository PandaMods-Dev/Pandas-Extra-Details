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

package me.pandamods.pandalib.client.animation.states;

import me.pandamods.pandalib.client.animation.AnimatableInstance;
import me.pandamods.pandalib.resource.animation.Animation;
import me.pandamods.pandalib.resource.AssimpResources;
import me.pandamods.pandalib.resource.animation.Channel;
import me.pandamods.pandalib.resource.model.Node;
import me.pandamods.pandalib.utils.PrintUtils;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class AnimationState extends State {
	private final Animation animation;
	private final boolean loop;

	public AnimationState(ResourceLocation resourceLocation) {
		this(resourceLocation, false);
	}

	public AnimationState(ResourceLocation resourceLocation, boolean loop) {
		this(AssimpResources.getAnimation(resourceLocation), loop);
	}

	public AnimationState(Animation animation, boolean loop) {
		this.animation = animation;
		this.loop = loop;
	}

	@Override
	public Matrix4f getBoneTransform(Node node) {
		Channel channel = this.animation.getChannel(node.getName());
		if (channel != null) {
			return channel.getMatrix(this.getTime());
		}
		return new Matrix4f(node.getInitialTransform());
	}

	@Override
	public void updateTime(AnimatableInstance instance, float partialTick) {
		super.updateTime(instance, partialTick);

		if (loop && this.isFinished()) {
			this.start(this.getStartTick() + (this.getDuration() * 20));
			this.updateTime(instance, partialTick);
		}
	}

	@Override
	public float getDuration() {
		return this.animation.getDuration();
	}
}

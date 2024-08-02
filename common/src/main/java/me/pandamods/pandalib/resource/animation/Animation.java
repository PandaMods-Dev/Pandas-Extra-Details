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

package me.pandamods.pandalib.resource.animation;

import org.joml.*;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.List;

public class Animation {
	private List<Channel> channels = new ArrayList<>();

	private float duration;

	public Animation() {}

	public Animation(List<Channel> channels, float duration) {
		set(channels, duration);
	}

	public Animation set(List<Channel> channels, float duration) {
		this.channels = channels;
		this.duration = duration;
		return this;
	}

	public float getDuration() {
		return duration;
	}

	public List<Channel> getChannels() {
		return channels;
	}

	public Channel getChannel(String name) {
		return channels.stream()
    			.filter(channel -> channel.name().equals(name))
    			.findFirst()
    			.orElse(null);
	}
}

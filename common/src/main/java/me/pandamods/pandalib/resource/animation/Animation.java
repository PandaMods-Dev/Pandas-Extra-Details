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

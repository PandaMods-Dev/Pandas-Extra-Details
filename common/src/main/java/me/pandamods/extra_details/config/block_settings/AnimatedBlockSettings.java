package me.pandamods.extra_details.config.block_settings;

import java.util.List;

public class AnimatedBlockSettings {
	public boolean enabled = true;
	public List<String> validBlocks;
	public float animationSpeed = 1;

	public AnimatedBlockSettings(List<String> validBlocks) {
		this.validBlocks = validBlocks;
	}
}

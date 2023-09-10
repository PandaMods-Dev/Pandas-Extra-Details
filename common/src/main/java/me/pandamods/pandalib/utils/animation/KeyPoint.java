package me.pandamods.pandalib.utils.animation;

import org.joml.Vector2f;

public class KeyPoint {
	public KeyType type;
	public Vector2f position;

	public KeyPoint(KeyType type, Vector2f position) {
		this.type = type;
		this.position = position;
	}
}

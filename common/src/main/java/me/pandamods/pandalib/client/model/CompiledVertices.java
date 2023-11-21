package me.pandamods.pandalib.client.model;

import org.joml.Vector2f;
import org.joml.Vector3f;

public record CompiledVertices(
		Vector3f position,
		Vector3f normal,
		Vector2f uv
) {
}

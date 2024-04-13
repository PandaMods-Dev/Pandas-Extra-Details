package me.pandamods.extra_details.pandalib.client.model;

import me.pandamods.extra_details.pandalib.resources.MeshRecord;
import org.joml.Vector3f;

public record CompiledVertex(
		Vector3f position,
		MeshRecord.Object.Vertex data) {
}

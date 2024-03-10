package me.pandamods.pandalib.resource;

import org.joml.*;

import java.util.List;
import java.util.Map;

public record MeshData(String formatVersion, Map<String, Object> objects) {
	public record Object(Vector3fc position, Quaternionfc rotation, List<Vertex> vertices, List<Face> faces) { }
	public record Face(Vector3fc normal, List<Integer> vertices, Map<Integer, Vector2fc> vertex_uvs, String texture_name) { }
	public record Vertex(int index, Vector3fc position, List<Weight> weights, float max_weight) { }
	public record Weight(String name, float weight) { }
}
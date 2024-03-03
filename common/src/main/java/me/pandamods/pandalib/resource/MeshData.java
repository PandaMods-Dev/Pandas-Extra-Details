package me.pandamods.pandalib.resource;

import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public record MeshData(String formatVersion, Map<String, Object> objects) {
	public record Object(Vector3f position, Vector3f rotation, List<Vertex> vertices, List<Face> faces) { }
	public record Face(Vector3f normal, List<Integer> vertices, Map<Integer, Vector2f> vertex_uvs, String texture_name) { }
	public record Vertex(int index, Vector3f position, List<Weight> weights, float max_weight) { }
	public record Weight(String name, float weight) { }
}
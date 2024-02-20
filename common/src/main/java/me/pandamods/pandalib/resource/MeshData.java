package me.pandamods.pandalib.resource;

import java.util.List;
import java.util.Map;

public record MeshData(String formatVersion, Map<String, Object> objects) {
	public record Object(float[] position, float[] rotation, List<Vertex> vertices, List<Face> faces) { }
	public record Face(float[] normal, List<Integer> vertices, Map<Integer, float[]> vertex_uvs, String texture_name) { }
	public record Vertex(int index, float[] position, List<Weight> weights, float max_weight) { }
	public record Weight(String name, float weight) { }
}
package me.pandamods.pandalib.resources;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public record MeshRecord(String format_version, Map<String, Object> objects) {
	public record Object(Vector3f position, Vector3f rotation, Vertex[] vertices, Face[] faces) {
		public record Face(Vector3f normal, int[] vertices, Map<Integer, float[]> vertex_uvs, String texture_name) {}
		public record Vertex(int index, Vector3f position, Weight[] weights, float max_weight) {
			public record Weight(String name, float weight) {}
		}
	}
}

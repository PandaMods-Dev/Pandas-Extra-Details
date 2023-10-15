package me.pandamods.pandalib.resources;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3f;

import java.util.Map;

@Environment(EnvType.CLIENT)
public record Mesh(String format_version, float scale, Map<String, Object> objects, Map<String, Bone> bone) {
	public record Object(Vector3f position, Vector3f rotation, Face[] faces) {
		public record Face(Vector3f normal, Map<Integer, Vertex> vertices, String texture_name) {
			public record Vertex(int index, Vector3f position, float[] uv, Weight[] weights) {
				public record Weight(String name, float weight) {}
			}
		}
	}

	public record Bone(Vector3f position, String parent) {}
}

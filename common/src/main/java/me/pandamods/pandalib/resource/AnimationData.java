package me.pandamods.pandalib.resource;

import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.Map;

public record AnimationData(String format_version, float animation_length, Map<String, Bone> bones) {
	public record Bone(Map<Float, Vector3fc> position, Map<Float, Quaternionfc> rotation, Map<Float, Vector3fc> scale) { }
}

package me.pandamods.pandalib.resource;

import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Map;

public record AnimationData(String format_version, float animation_length, Map<String, Bone> bones) {
	public record Bone(Map<Float, Vector3f> position, Map<Float, Quaternionf> rotation, Map<Float, Vector3f> scale) { }
}

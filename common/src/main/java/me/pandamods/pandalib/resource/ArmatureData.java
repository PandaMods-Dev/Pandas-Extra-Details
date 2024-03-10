package me.pandamods.pandalib.resource;

import org.joml.Matrix4fc;
import org.joml.Vector3f;

import java.util.Map;

public record ArmatureData(String format_version, Map<String, Bone> bones) {
	public record Bone(Matrix4fc transform, String parent) {}
}
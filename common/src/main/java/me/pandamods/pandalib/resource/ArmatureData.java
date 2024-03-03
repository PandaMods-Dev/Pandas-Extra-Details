package me.pandamods.pandalib.resource;

import org.joml.Vector3f;

import java.util.Map;

public record ArmatureData(String format_version, Map<String, Bone> bones) {
	public record Bone(Vector3f position, String parent) {}
}
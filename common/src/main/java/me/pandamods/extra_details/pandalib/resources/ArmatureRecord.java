package me.pandamods.extra_details.pandalib.resources;

import org.joml.Vector3f;

import java.util.Map;

public record ArmatureRecord(String format_version, Map<String, Bone> bones) {
	public record Bone(Vector3f position, String parent) {}
}

package me.pandamods.pandalib.resources;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Vector3f;

import java.util.Map;

@Environment(EnvType.CLIENT)
public record RawAnimation(String format_version, float animation_length, Map<String, Bone> bones) {
	public record Bone(Map<Float, Vector3f> position, Map<Float, Vector3f> rotation, Map<Float, Vector3f> scale) {
	}
}

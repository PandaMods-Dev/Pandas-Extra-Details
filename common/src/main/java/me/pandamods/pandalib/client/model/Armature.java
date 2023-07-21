package me.pandamods.pandalib.client.model;

import me.pandamods.pandalib.resources.Mesh;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class Armature {
	private final Map<String, Bone> bones = new HashMap<>();

	public Armature(Mesh mesh) {
		mesh.bone().forEach((s, bone) -> bones.put(s, new Bone(this, s, bone)));
	}

	public Optional<Bone> getBone(String name) {
		return Optional.ofNullable(bones.getOrDefault(name, null));
	}
}

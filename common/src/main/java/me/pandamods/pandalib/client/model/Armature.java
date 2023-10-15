package me.pandamods.pandalib.client.model;

import me.pandamods.pandalib.resources.Mesh;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.*;

@Environment(EnvType.CLIENT)
public class Armature {
	private final Map<String, Bone> bones = new HashMap<>();
	private final List<String> updatedBones = new ArrayList<>();

	public Armature(Mesh mesh) {
		mesh.bone().forEach((s, bone) -> bones.put(s, new Bone(this, s, bone)));
	}

	public Optional<Bone> getBone(String name) {
		return Optional.ofNullable(bones.getOrDefault(name, null));
	}

	public void updateBone(Bone bone) {
		updateBone(bone.getName());
	}

	public void updateBone(String name) {
		if (!updatedBones.contains(name)) {
			updatedBones.add(name);
		}
	}

	public boolean isUpdated(Bone bone) {
		return isUpdated(bone.getName());
	}

	public boolean isUpdated(String name) {
		return updatedBones.contains(name);
	}

	public void clearUpdatedBones() {
		updatedBones.clear();
	}
}

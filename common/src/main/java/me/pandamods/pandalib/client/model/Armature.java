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
		updatedBones.addAll(bones.keySet());
	}

	public Optional<Bone> getBone(String name) {
		return Optional.ofNullable(bones.getOrDefault(name, null));
	}

	public void updateBone(Bone bone) {
		if (!updatedBones.contains(bone.getName())) {
			updatedBones.add(bone.getName());
			for (Map.Entry<String, Bone> entry : bones.entrySet()) {
				Optional<Bone> childBonesParent = entry.getValue().getParent();
				if (childBonesParent.isPresent() && childBonesParent.get().getName().equals(bone.getName())) {
					updateBone(entry.getValue());
				}
			}
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

	public Map<String, Bone> getBones() {
		return new HashMap<>(bones);
	}
}

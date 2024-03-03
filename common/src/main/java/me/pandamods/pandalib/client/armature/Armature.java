package me.pandamods.pandalib.client.armature;

import me.pandamods.pandalib.resource.ArmatureData;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Armature {
	private final Map<String, Bone> bones = new HashMap<>();

	public Armature(ArmatureData armatureData) {
		armatureData.bones().forEach((name, boneData) -> {
			Bone parent = bones.get(boneData.parent());
			bones.put(name, new Bone(parent, name, boneData));
		});
	}

	public Optional<Bone> getBone(String name) {
		return Optional.ofNullable(bones.get(name));
	}

	public Map<String, Bone> getBones() {
		return bones;
	}
}

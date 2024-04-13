package me.pandamods.extra_details.pandalib.client.model;

import me.pandamods.extra_details.pandalib.resources.ArmatureRecord;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.*;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class Armature {
	private final Set<Bone> bones = new HashSet<>();
	private final Set<Bone> updatedBones = new HashSet<>();
	private final Set<String> hiddenObjects = new HashSet<>();

	protected boolean mirrorXTranslation = false;
	protected boolean mirrorYTranslation = false;
	protected boolean mirrorZTranslation = false;

	protected boolean mirrorXRotation = false;
	protected boolean mirrorYRotation = false;
	protected boolean mirrorZRotation = false;

	protected boolean mirrorXScale = false;
	protected boolean mirrorYScale = false;
	protected boolean mirrorZScale = false;

	public Armature(ArmatureRecord armature) {
		armature.bones().forEach((s, bone) -> bones.add(new Bone(this, s, bone)));
		updatedBones.addAll(bones);
	}

	public Optional<Bone> getBone(String name) {
		return Optional.ofNullable(getBones().getOrDefault(name, null));
	}

	public void updateBone(Bone bone) {
		updatedBones.add(bone);
		for (Bone child : bone.getChildren()) {
			child.updateBone();
		}
	}


	public boolean isUpdated(Bone bone) {
		return this.updatedBones.contains(bone);
	}

	public boolean isUpdated(String name) {
		return updatedBones.stream().anyMatch(bone -> bone.getName().equals(name));
	}

	public void clearUpdatedBones() {
		updatedBones.clear();
	}

	public Map<String, Bone> getBones() {
		return bones.stream().collect(Collectors.toMap(Bone::getName, bone -> bone));
	}

	public void setHidden(String... objectNames) {
		setVisibility(false, objectNames);
	}

	public void setVisible(String... objectNames) {
		setVisibility(true, objectNames);
	}

	public void setVisibility(boolean visible, String... objectNames) {
		if (visible)
			List.of(objectNames).forEach(this.hiddenObjects::remove);
		else
			this.hiddenObjects.addAll(List.of(objectNames));
	}

	public boolean getVisibility(String objectName) {
		return this.hiddenObjects.contains(objectName);
	}

	public void mirrorX(boolean translation, boolean rotation, boolean scale) {
		this.mirrorXTranslation = translation;
		this.mirrorXRotation = rotation;
		this.mirrorXScale = scale;
	}

	public void mirrorY(boolean translation, boolean rotation, boolean scale) {
		this.mirrorYTranslation = translation;
		this.mirrorYRotation = rotation;
		this.mirrorYScale = scale;
	}

	public void mirrorZ(boolean translation, boolean rotation, boolean scale) {
		this.mirrorZTranslation = translation;
		this.mirrorZRotation = rotation;
		this.mirrorZScale = scale;
	}
}

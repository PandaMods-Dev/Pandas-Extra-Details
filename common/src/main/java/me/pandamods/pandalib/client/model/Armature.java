package me.pandamods.pandalib.client.model;

import me.pandamods.pandalib.resources.Mesh;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.*;

@Environment(EnvType.CLIENT)
public class Armature {
	private final Map<String, Bone> bones = new HashMap<>();
	private final Set<String> updatedBones = new HashSet<>();
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

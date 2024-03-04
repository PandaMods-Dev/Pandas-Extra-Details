package me.pandamods.pandalib.client.armature;

import me.pandamods.pandalib.resource.ArmatureData;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

public class Bone {
	private final Matrix4f initialTransform;
	public final Matrix4f localTransform;
	private final Matrix4f globalTransform;
	public final Map<String, Bone> children = new HashMap<>();
	public final Bone parent;

	public Bone(Bone parent, String name, ArmatureData.Bone boneData) {
		this.parent = parent;
		if (this.parent != null)
			this.parent.children.put(name, this);
		this.initialTransform = new Matrix4f().identity().translate(boneData.position());
		this.localTransform = new Matrix4f().identity();
		this.globalTransform = new Matrix4f().identity();
		this.updateGlobalTransform();
	}

	public Matrix4f getGlobalTransform() {
		return globalTransform;
	}

	public void updateGlobalTransform() {
		// recode
		children.values().forEach(Bone::updateGlobalTransform);
	}

	public Matrix4f getInitialTransform() {
		return initialTransform;
	}
}
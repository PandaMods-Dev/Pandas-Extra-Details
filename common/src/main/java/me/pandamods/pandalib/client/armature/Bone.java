package me.pandamods.pandalib.client.armature;

import me.pandamods.pandalib.resource.ArmatureData;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bone {
	private final Vector3f pivotPoint;
	public final Matrix4f localTransform;
	private final Matrix4f globalTransform;
	public final Map<String, Bone> children = new HashMap<>();
	public final Bone parent;

	public Bone(Bone parent, String name, ArmatureData.Bone boneData) {
		this.parent = parent;
		if (this.parent != null)
			this.parent.children.put(name, this);
		this.pivotPoint = boneData.position();
		this.localTransform = new Matrix4f().identity();
		this.globalTransform = new Matrix4f().identity();
		this.updateGlobalTransform();
	}

	public Matrix4f getGlobalTransform() {
		return globalTransform;
	}

	public void updateGlobalTransform() {
		if (parent != null) {
			globalTransform.set(parent.getGlobalTransform()).mul(localTransform);
		} else {
			globalTransform.set(localTransform);
		}
		globalTransform.translate(pivotPoint);
		children.values().forEach(Bone::updateGlobalTransform);
	}

	public Vector3f getPivotPoint() {
		return pivotPoint;
	}
}
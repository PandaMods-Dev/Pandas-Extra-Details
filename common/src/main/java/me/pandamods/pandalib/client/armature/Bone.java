package me.pandamods.pandalib.client.armature;

import me.pandamods.pandalib.resource.ArmatureData;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import java.lang.invoke.MethodHandles;
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
		this.initialTransform = new Matrix4f(boneData.transform());
		this.localTransform = new Matrix4f().identity();
		this.globalTransform = new Matrix4f().identity();
		this.updateTransform();
	}

	public Matrix4fc getGlobalTransform() {
		return globalTransform;
	}

	public Matrix4fc getInitialTransform() {
		return initialTransform;
	}

	public void updateTransform() {
		this.globalTransform.identity();
		if (parent != null) {
			this.globalTransform.mul(parent.globalTransform).mul(new Matrix4f(parent.initialTransform.invert(new Matrix4f())));
		}
		this.globalTransform.mul(initialTransform);
		this.globalTransform.mul(localTransform);

		children.values().forEach(Bone::updateTransform);
	}
}
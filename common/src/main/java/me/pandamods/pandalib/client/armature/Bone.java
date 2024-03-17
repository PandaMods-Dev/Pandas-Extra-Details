package me.pandamods.pandalib.client.armature;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.pandalib.resource.ArmatureData;
import org.joml.*;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Bone {
	private final ArmatureData.Bone boneData;

	private final String name;
	public final Matrix4f initialTransform;
	public final Matrix4f localTransform;
	private final Matrix4f globalTransform;
	public final Map<String, Bone> children = new HashMap<>();
	public final Bone parent;

	public Bone(Bone parent, String name, ArmatureData.Bone boneData) {
		this.name = name;
		this.parent = parent;
		this.boneData = boneData;
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
	public Matrix4f resetInitialTransform() {
		return this.initialTransform.set(this.boneData.transform());
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

	public PoseStack applyToPoseStack(PoseStack poseStack) {
		poseStack.translate(0.5, 0, 0.5);
		Vector3f offset = this.initialTransform.getTranslation(new Vector3f());
		poseStack.translate(offset.x, offset.y, offset.z);
		poseStack.mulPoseMatrix(this.localTransform);
		poseStack.translate(-offset.x, -offset.y, -offset.z);
		poseStack.translate(-0.5, 0, -0.5);
		return poseStack;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Bone bone = (Bone) o;
		return Objects.equals(name, bone.name) &&
				Objects.equals(parent, bone.parent);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, parent);
	}
}
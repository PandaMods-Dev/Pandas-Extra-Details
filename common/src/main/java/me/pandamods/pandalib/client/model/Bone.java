package me.pandamods.pandalib.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.pandalib.resources.Mesh;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class Bone {
	private final String name;
	private final Matrix4f offsetTransform;
	private Matrix4f localTransform = new Matrix4f();
	private final Bone defaultParent;
	private Bone parent;
	private final Armature armature;

	public Bone(Armature armature, String name, Matrix4f offsetMatrix, Bone parent) {
		this.name = name;
		this.offsetTransform = offsetMatrix;
		this.parent = parent;
		this.defaultParent = parent;
		this.armature = armature;
	}

	public Bone(Armature armature, String name, Mesh.Bone bone) {
		this(
				armature,
				name,
				new Matrix4f()
						.identity()
						.translate(bone.position()),
				armature.getBone(bone.parent()).orElse(null)
		);
	}

	public Optional<Bone> getParent() {
		return Optional.ofNullable(parent);
	}
	public void setParent(Bone bone) {
		this.parent = bone;
	}
	public void resetParent() {
		this.parent = this.defaultParent;
	}

	public Matrix4f getOffsetTransform() {
		return new Matrix4f(offsetTransform);
	}

	public Matrix4f getLocalTransform() {
        return new Matrix4f(localTransform);
    }


	public void setLocalTransform(Matrix4f localTransform) {
		if (!this.localTransform.equals(localTransform))
			this.armature.updateBone(this);
		this.localTransform = localTransform;
	}

	public void setTranslation(float x, float y, float z) {
		setLocalTransform(this.getLocalTransform().setTranslation(x, y, z));
	}

	public void setRotation(float x, float y, float z) {
		setLocalTransform(this.getLocalTransform().setRotationZYX(z, y, x));
	}

	public Vector3f getRotation() {
		return this.getLocalTransform().getEulerAnglesXYZ(new Vector3f());
	}

	public Matrix4f getWorldTransform() {
		Matrix4f offsetTransform = this.getOffsetTransform();
		Matrix4f offsetInverse = new Matrix4f(offsetTransform).invert();

        if (getParent().isPresent()) {
            Matrix4f parentWorldTransform = getParent().get().getWorldTransform();

            Matrix4f worldTransform = new Matrix4f(parentWorldTransform);
			Matrix4f finalTransform = new Matrix4f(offsetTransform).mul(getLocalTransform()).mul(offsetInverse);

            worldTransform.mul(finalTransform);

            return worldTransform;
        } else {
            return new Matrix4f(offsetTransform).mul(getLocalTransform()).mul(offsetInverse);
        }
    }

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Bone{" +
				"name=" + name +
				", parent=" + parent.name +
				", offsetMatrix=" + offsetTransform +
				", localTransform=" + getLocalTransform() +
				", worldTransform=" + getWorldTransform() +
				'}';
	}

	public void applyToPoseStack(PoseStack poseStack) {
		poseStack.translate(0.5, 0, 0.5);
		Vector3f offset = this.getOffsetTransform().getTranslation(new Vector3f());
		poseStack.translate(offset.x, offset.y, offset.z);
		poseStack.mulPoseMatrix(this.getLocalTransform());
	}
}

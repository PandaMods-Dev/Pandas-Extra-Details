package me.pandamods.pandalib.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.pandalib.resources.Mesh;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;
import org.joml.Quaterniond;
import org.joml.Quaternionf;
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
		return mirrorMatrix(offsetTransform);
	}

	public Matrix4f getLocalTransform() {
		return mirrorMatrix(localTransform);
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
				", parent=" + (parent != null ? parent.name : "None") +
				", offsetMatrix=" + offsetTransform +
				", localTransform=" + getLocalTransform() +
				", worldTransform=" + getWorldTransform() +
				'}';
	}

	public void applyToPoseStack(PoseStack poseStack) {
		poseStack.translate(0.5, 0, 0.5);
		Vector3f offset = this.getOffsetTransform().getTranslation(new Vector3f());
		poseStack.translate(offset.x, offset.y, offset.z);

		Matrix4f matrix = this.getLocalTransform();
		Vector3f translation = matrix.getTranslation(new Vector3f());
		Quaternionf rotation = matrix.getNormalizedRotation(new Quaternionf());
		Vector3f scale = matrix.getScale(new Vector3f());

		poseStack.translate(translation.x, translation.y, translation.z);
		poseStack.mulPose(rotation);
		poseStack.scale(scale.x, scale.y, scale.z);

		poseStack.translate(-offset.x, -offset.y, -offset.z);
		poseStack.translate(-0.5, 0, -0.5);
	}

	public Matrix4f mirrorMatrix(Matrix4f matrix) {
		Vector3f translation = matrix.getTranslation(new Vector3f());
		Quaternionf rotation = matrix.getNormalizedRotation(new Quaternionf());
		Vector3f scale = matrix.getScale(new Vector3f());

		float mirrorXTranslationFactor = armature.mirrorXTranslation ? -1 : 1;
		float mirrorYTranslationFactor = armature.mirrorYTranslation ? -1 : 1;
		float mirrorZTranslationFactor = armature.mirrorZTranslation ? -1 : 1;

		float mirrorZRotationFactor = armature.mirrorZRotation ? -1 : 1;
		float mirrorYRotationFactor = armature.mirrorYRotation ? -1 : 1;
		float mirrorXRotationFactor = armature.mirrorXRotation ? -1 : 1;

		float mirrorXScaleFactor = armature.mirrorXScale ? -1 : 1;
		float mirrorYScaleFactor = armature.mirrorYScale ? -1 : 1;
		float mirrorZScaleFactor = armature.mirrorZScale ? -1 : 1;

		Matrix4f translationMatrix = new Matrix4f().translation(
				translation.x * mirrorXTranslationFactor,
				translation.y * mirrorYTranslationFactor,
				translation.z * mirrorZTranslationFactor
		);

		Quaternionf mirroredRotation = new Quaternionf(
				rotation.x * mirrorXRotationFactor,
				rotation.y * mirrorYRotationFactor,
				rotation.z * mirrorZRotationFactor,
				rotation.w
		);
		mirroredRotation.normalize();
		Matrix4f rotationMatrix = new Matrix4f().rotation(mirroredRotation);

		Matrix4f scalingMatrix = new Matrix4f().scaling(
				scale.x * mirrorXScaleFactor,
				scale.y * mirrorYScaleFactor,
				scale.z * mirrorZScaleFactor
		);

		return new Matrix4f(translationMatrix).mul(rotationMatrix).mul(scalingMatrix);
	}
}

package me.pandamods.extra_details.pandalib.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.pandalib.resources.ArmatureRecord;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Environment(EnvType.CLIENT)
public class Bone {
	private final String name;
	private Matrix4f offsetTransform;
	private Matrix4f localTransform = new Matrix4f();
	private final Bone defaultParent;
	private Bone parent = null;
	private Set<Bone> children = new HashSet<>();
	private final Armature armature;

	public Bone(Armature armature, String name, Matrix4f offsetMatrix, Bone parent) {
		this.armature = armature;
		this.name = name;
		this.offsetTransform = offsetMatrix;
		this.defaultParent = parent;
		this.resetParent();
	}

	public Bone(Armature armature, String name, ArmatureRecord.Bone bone) {
		this(
				armature,
				name,
				new Matrix4f()
						.identity()
						.translate(bone.position()),
				armature.getBone(bone.parent()).orElse(null)
		);
	}

	public Armature getArmature() {
		return armature;
	}

	public void updateBone() {
		this.armature.updateBone(this);
	}

	public Optional<Bone> getParent() {
		return Optional.ofNullable(parent);
	}
	public void setParent(Bone bone) {
		if (!Objects.equals(this.parent, bone))
			this.armature.updateBone(this);

		if (this.parent != null)
			this.parent.children.remove(this);
		this.parent = bone;
		if (this.parent != null)
			this.parent.children.add(this);
	}
	public void resetParent() {
		this.setParent(this.defaultParent);
	}

	public Set<Bone> getChildren() {
		return new HashSet<>(children);
	}

	public Matrix4f offsetTransform() {
		return new Matrix4f(this.offsetTransform);
	}

	public Matrix4f offsetTransform(Function<Matrix4f, Matrix4f> matrixFunction) {
		Matrix4f matrix = matrixFunction.apply(new Matrix4f(this.offsetTransform));
		if (!this.offsetTransform.equals(matrix))
			this.armature.updateBone(this);
		return new Matrix4f(this.offsetTransform = matrix);
	}

	public Matrix4f localTransform() {
		return new Matrix4f(this.localTransform);
	}

	public Matrix4f localTransform(Function<Matrix4f, Matrix4f> matrixFunction) {
		Matrix4f matrix = matrixFunction.apply(new Matrix4f(this.localTransform));
		if (!this.localTransform.equals(matrix))
			this.armature.updateBone(this);
		return new Matrix4f(this.localTransform = matrix);
	}

	public Matrix4f getWorldTransform() {
		Matrix4f offsetTransform = mirrorMatrix(this.offsetTransform());
		Matrix4f offsetInverse = offsetTransform.invert(new Matrix4f());

        if (getParent().isPresent()) {
            Matrix4f parentWorldTransform = getParent().get().getWorldTransform();

            Matrix4f worldTransform = new Matrix4f(parentWorldTransform);
			Matrix4f finalTransform = new Matrix4f(offsetTransform).mul(mirrorMatrix(localTransform())).mul(offsetInverse);

            worldTransform.mul(finalTransform);

			return worldTransform;
        } else {
			return new Matrix4f(offsetTransform).mul(mirrorMatrix(localTransform())).mul(offsetInverse);
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
				", offsetTransform=" + offsetTransform() +
				", localTransform=" + localTransform() +
				", worldTransform=" + getWorldTransform() +
				'}';
	}

	public void applyToPoseStack(PoseStack poseStack) {
		poseStack.translate(0.5, 0, 0.5);
		Vector3f offset = mirrorMatrix(this.offsetTransform()).getTranslation(new Vector3f());
		poseStack.translate(offset.x, offset.y, offset.z);

		Matrix4f matrix = mirrorMatrix(this.localTransform());
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

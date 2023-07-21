package me.pandamods.pandalib.client.model;

import me.pandamods.pandalib.resources.Mesh;
import me.pandamods.pandalib.utils.VectorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.AxisAngle4d;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Optional;

@Environment(EnvType.CLIENT)
public class Bone {
	private final String name;
	private final Matrix4f offsetTransform;
    public Matrix4f localTransform = new Matrix4f();
	public Optional<Bone> parent;

	public Bone(String name, Matrix4f offsetMatrix, Optional<Bone> parent) {
		this.name = name;
		this.offsetTransform = offsetMatrix;
		this.parent = parent;
	}

	public Bone(Armature armature, String name, Mesh.Bone bone) {
		this(
				name,
				new Matrix4f()
						.identity()
						.translate(bone.position()),
				armature.getBone(bone.parent())
		);
	}

	public Matrix4f getOffsetTransform() {
		return new Matrix4f(offsetTransform);
	}

	public Matrix4f getLocalTransform() {
        return new Matrix4f(localTransform);
    }

    public Matrix4f getWorldTransform() {
		Matrix4f offsetTransform = this.getOffsetTransform();
		Matrix4f offsetInverse = new Matrix4f(offsetTransform).invert();

        if (parent.isPresent()) {
            Matrix4f parentWorldTransform = parent.get().getWorldTransform();

            Matrix4f worldTransform = new Matrix4f(parentWorldTransform);
			Matrix4f finalTransform = new Matrix4f(offsetTransform).mul(getLocalTransform()).mul(offsetInverse);

            worldTransform.mul(finalTransform);

            return worldTransform;
        } else {
            return new Matrix4f(offsetTransform).mul(getLocalTransform()).mul(offsetInverse);
        }
    }

	@Override
	public String toString() {
		return "NewBone{" +
				"name=" + name +
				", parent=" + parent +
				", offsetMatrix=" + offsetTransform +
				", localTransform=" + getLocalTransform() +
				", worldTransform=" + getWorldTransform() +
				'}';
	}
}

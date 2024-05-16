package me.pandamods.pandalib.resource;

import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.joml.*;
import org.lwjgl.assimp.*;

import java.util.*;
import java.util.function.Function;

public class Mesh {
	private final Object[] objects;
//	private final Bone rootBone;
	private final Map<String, Bone> bones = new Object2ObjectOpenHashMap<>();

	public Mesh(AIScene scene) {
		List<Object> objects = new ArrayList<>();
//		this.rootBone = scene.mRootNode() == null ? null : createBone(scene.mRootNode(), null);

		for (int i = 0; i < scene.mNumMeshes(); i++) {
			AIMesh mesh = AIMesh.create(scene.mMeshes().get(i));

			AIMaterial material = AIMaterial.create(scene.mMaterials().get(mesh.mMaterialIndex()));
			AIString materialName = AIString.create();
			Assimp.aiGetMaterialString(material, Assimp.AI_MATKEY_NAME, Assimp.aiTextureType_NONE, 0, materialName);
			String materialNameStr = materialName.dataString();

			objects.add(new Object(mesh, materialNameStr));
		}

		this.objects = objects.toArray(new Object[0]);
	}

	private void processBone(AIBone aiBone) {
		String name = aiBone.mName().dataString();
		Bone parent = aiBone.mNode().mParent() != null ?
				bones.get(aiBone.mNode().mParent().mName().dataString()) : null;

		AIMatrix4x4 matrix = aiBone.mOffsetMatrix();
		Bone bone = new Bone(name, new Matrix4f(
				matrix.a1(), matrix.a2(), matrix.a3(), matrix.a4(),
				matrix.b1(), matrix.b2(), matrix.b3(), matrix.b4(),
				matrix.c1(), matrix.c2(), matrix.c3(), matrix.c4(),
				matrix.d1(), matrix.d2(), matrix.d3(), matrix.d4()
		), parent);

		bones.put(name, bone);
	}

	public void render(Matrix4f worldMatrix, Matrix3f normalMatrix, int overlayUV, int lightmapUV,
					   Function<String, VertexConsumer> vertexConsumerProvider) {
		for (Object object : this.objects) {
			object.render(vertexConsumerProvider.apply(object.materialName), worldMatrix, normalMatrix, overlayUV, lightmapUV);
		}
	}

	public Bone getBone(String name) {
		return bones.get(name);
	}

	public class Object {
		private final Integer[] indices;
		private final Float[] vertices;
		private final Float[] uvs;
		private final Float[] normals;
		private final Weight[] weights;
		private final String materialName;

		public Object(AIMesh mesh, String materialName) {
			List<Integer> indices = new ArrayList<>();
			List<Float> vertices = new ArrayList<>();
			List<Float> uvs = new ArrayList<>();
			List<Float> normals = new ArrayList<>();
			List<Weight> weights = new ArrayList<>();

			for (AIFace face : mesh.mFaces()) {
				for (int i = 0; i < face.mNumIndices(); i++) {
					indices.add(face.mIndices().get(i));
				}
			}

			for (AIVector3D position : mesh.mVertices()) {
				vertices.add(position.x());
				vertices.add(position.y());
				vertices.add(position.z());
			}

			for (AIVector3D uv : mesh.mTextureCoords(0)) {
				uvs.add(uv.x());
				uvs.add(1 - uv.y());
			}

			for (AIVector3D normal : mesh.mNormals()) {
				normals.add(normal.x());
				normals.add(normal.y());
				normals.add(normal.z());
			}

			for (int i = 0; i < mesh.mNumBones(); i++) {
				AIBone bone = AIBone.create(mesh.mBones().get(i));
				processBone(bone);
				String name = bone.mName().dataString();
				for (AIVertexWeight mWeight : bone.mWeights()) {
					weights.add(new Weight(name, mWeight.mVertexId(), mWeight.mWeight()));
				}
			}

			this.indices = indices.toArray(new Integer[0]);
			this.vertices = vertices.toArray(new Float[0]);
			this.uvs = uvs.toArray(new Float[0]);
			this.normals = normals.toArray(new Float[0]);
			this.weights = weights.toArray(new Weight[0]);
			this.materialName = materialName;
		}

		private void render(VertexConsumer vertexConsumer, Matrix4f worldMatrix, Matrix3f normalMatrix, int overlayUV, int lightmapUV) {
			Matrix4f offsetMatrix = new Matrix4f();
			Matrix4f globalMatrix = new Matrix4f();

			Vector3f transformedPosition = new Vector3f();

			Quaternionf normalRotation = new Quaternionf();
			Quaternionf transformedNormalRotation = new Quaternionf();

			for (Integer i : this.indices) {
				float posX = this.vertices[i * 3];
				float posY = this.vertices[i * 3 + 1];
				float posZ = this.vertices[i * 3 + 2];

				float u = this.uvs[i * 2];
				float v = this.uvs[i * 2 + 1];

				float normX = this.normals[i * 3];
				float normY = this.normals[i * 3 + 1];
				float normZ = this.normals[i * 3 + 2];

				if (weights.length != 0) {
					float finalPosX = 0;
					float finalPosY = 0;
					float finalPosZ = 0;
					normalRotation.identity();
					for (Weight weight : weights) {
						Bone bone = getBone(weight.boneName);
						if (weight.index != i || !weight.boneName.equals(bone.name)) continue;
						float weightValue = weight.value;

						offsetMatrix.set(bone.offsetMatrix);
						globalMatrix.set(bone.getGlobalMatrix());
						globalMatrix.mul(offsetMatrix.invert());

						transformedPosition.set(posX, posY, posZ);
						globalMatrix.transformPosition(transformedPosition);

						transformedPosition.mul(weightValue);
						finalPosX += transformedPosition.x;
						finalPosY += transformedPosition.y;
						finalPosZ += transformedPosition.z;

						transformedNormalRotation.set(normalRotation);
						globalMatrix.rotate(transformedNormalRotation);

						transformedNormalRotation.mul(weightValue);
						normalRotation.mul(transformedNormalRotation);
					}
					posX = finalPosX;
					posY = finalPosY;
					posZ = finalPosZ;
				}

				vertexConsumer
						.vertex(worldMatrix, posX, posY, posZ)
						.color(255, 255, 255, 255)
						.uv(u, v)
						.overlayCoords(overlayUV)
						.uv2(lightmapUV)
						.normal(normalMatrix, normX, normY, normZ)
						.endVertex();
			}
		}
	}

	public class Bone {
		private final String name;
		private final Bone parent;
		private List<Bone> children = new ObjectArrayList<>();

		private final Matrix4fc offsetMatrix;
		private final Matrix4f globalMatrix = new Matrix4f().identity();
		public final Matrix4f localMatrix = new Matrix4f().identity();

		public Bone(String name, Matrix4f offsetMatrix, Bone parent) {
			this.name = name;
			this.parent = parent;
			this.offsetMatrix = offsetMatrix;

			if (parent != null)
				parent.children.add(this);
		}

		public Bone getParent() {
			return parent;
		}

		public Matrix4fc getGlobalMatrix() {
			this.globalMatrix.identity();
			if (parent != null) {
				this.globalMatrix.mul(parent.getGlobalMatrix()).mul(new Matrix4f(parent.offsetMatrix.invert(new Matrix4f())));
			}
			this.globalMatrix.mul(this.offsetMatrix);
			this.globalMatrix.mul(this.localMatrix);
			return this.globalMatrix;
		}
	}

	public record Weight(String boneName, int index, float value) {}
}

package me.pandamods.pandalib.resource;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.joml.*;
import org.joml.Math;
import org.lwjgl.assimp.*;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class Mesh {
	private final List<Object> objects = new ObjectArrayList<>();
	private final Map<String, Bone> bones = new Object2ObjectOpenHashMap<>();

	public Mesh() {}

	public Mesh(List<AIMesh> meshes, List<AIMaterial> materials) {
		set(meshes, materials);
	}

	public Mesh set(List<AIMesh> meshes, List<AIMaterial> materials) {
		objects.clear();
		for (AIMesh mesh : meshes) {
			AIMaterial material = materials.get(mesh.mMaterialIndex());
			AIString materialName = AIString.create();
			Assimp.aiGetMaterialString(material, Assimp.AI_MATKEY_NAME, Assimp.aiTextureType_NONE, 0, materialName);
			String materialNameStr = materialName.dataString();

			objects.add(new Object(mesh, materialNameStr));
		}
		return this;
	}

	private void processBone(AIBone aiBone) {
		String name = aiBone.mName().dataString();
		Bone parent = aiBone.mNode().mParent() != null ?
				bones.get(aiBone.mNode().mParent().mName().dataString()) : null;

		AIMatrix4x4 matrix = aiBone.mOffsetMatrix();
		Bone bone = new Bone(name, new Matrix4f(
				matrix.a1(), matrix.b1(), matrix.c1(), matrix.d1(),
				matrix.a2(), matrix.b2(), matrix.c2(), matrix.d2(),
				matrix.a3(), matrix.b3(), matrix.c3(), matrix.d3(),
				matrix.a4(), matrix.b4(), matrix.c4(), matrix.d4()
		), parent);

		bones.put(name, bone);
	}

	public void render(PoseStack poseStack, int overlayUV, int lightmapUV, Function<String, VertexConsumer> vertexConsumerProvider) {
		PoseStack.Pose last = poseStack.last();
		render(last.pose(), last.normal(), overlayUV, lightmapUV, vertexConsumerProvider);
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

	public void forEachBone(BiConsumer<String, Bone> action) {
		bones.forEach(action);
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

						transformedPosition.set(posX, posY, posZ);
						bone.getOffsetMatrix().transformPosition(transformedPosition);
						bone.getGlobalMatrix(true).transformPosition(transformedPosition);

						transformedPosition.mul(weightValue);
						finalPosX += transformedPosition.x;
						finalPosY += transformedPosition.y;
						finalPosZ += transformedPosition.z;

						transformedNormalRotation.set(normalRotation);

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

		private final Matrix4f offsetMatrix;
		private final Matrix4f localMatrix;
		private final Matrix4f globalMatrix;

		public Bone(String name, Matrix4f offsetMatrix, Bone parent) {
			this.name = name;
			this.parent = parent;
			this.offsetMatrix = offsetMatrix;
			this.localMatrix = new Matrix4f();
			this.globalMatrix = new Matrix4f();
			updateGlobalMatrix();

			if (parent != null)
				parent.children.add(this);
		}

		public String getName() {
			return name;
		}

		public Bone getParent() {
			return parent;
		}

		private void updateGlobalMatrix() {
			if (parent != null) {
				parent.getGlobalMatrix().mul(localMatrix, globalMatrix);
			} else {
				globalMatrix.set(localMatrix);
			}

			children.forEach(Bone::updateGlobalMatrix);
		}

		public Matrix4fc getGlobalMatrix() {
			return getGlobalMatrix(false);
		}

		public Matrix4fc getGlobalMatrix(Boolean shouldCalculate) {
			if (shouldCalculate) updateGlobalMatrix();
			return globalMatrix;
		}

		public Matrix4f getLocalMatrix() {
			return localMatrix;
		}

		public Matrix4fc getOffsetMatrix() {
			return offsetMatrix;
		}

		public void resetMatrix() {
			this.localMatrix.set(this.offsetMatrix);
		}
	}

	public record Weight(String boneName, int index, float value) {}
}

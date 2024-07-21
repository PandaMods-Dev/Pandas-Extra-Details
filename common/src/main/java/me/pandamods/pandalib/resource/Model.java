package me.pandamods.pandalib.resource;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.pandalib.utils.AssimpUtils;
import org.joml.*;
import org.joml.Math;
import org.lwjgl.assimp.*;

import java.text.NumberFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class Model {
	private List<Mesh> meshes = new ArrayList<>();
	private List<Material> materials = new ArrayList<>();
	private Map<String, Skeleton> skeletons = new HashMap<>();

	public Model() {}

	public Model(AIScene scene) {
		set(scene);
	}

	public void render(PoseStack poseStack, int overlayUV, int lightmapUV,
					   Function<String, VertexConsumer> vertexConsumerProvider) {
		for (Skeleton skeleton : getSkeletonsMap().values()) {
			skeleton.updateGlobalTransform();
		}

		for (Material material : this.materials) {
			VertexConsumer vertexConsumer = vertexConsumerProvider.apply(material.getName());
			for (Mesh mesh : material.getMeshList()) {
				mesh.render(vertexConsumer, poseStack, overlayUV, lightmapUV);
			}
		}
	}

	public Model set(AIScene scene) {
		List<Mesh> meshes = new ArrayList<>();
		List<Material> materials = new ArrayList<>();
		Map<String, Skeleton> skeletons = new HashMap<>();

		// Process Materials
		for (int i = 0; i < scene.mNumMaterials(); i++) {
			AIMaterial material = AIMaterial.create(scene.mMaterials().get(i));
			materials.add(Material.process(material));
		}

		// Process Meshes
		Material defaultMaterial = new Material("pandalib.default");
		for (int i = 0; i < scene.mNumMeshes(); i++) {
			AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(i));
			Mesh mesh = Mesh.process(aiMesh, skeletons);
			meshes.add(mesh);

			int materialIdx = aiMesh.mMaterialIndex();
			Material material;
			if (materialIdx >= 0 && materialIdx < materials.size()) {
				material = materials.get(materialIdx);
			} else {
				material = defaultMaterial;
			}

			material.getMeshList().add(mesh);

			if (!defaultMaterial.getMeshList().isEmpty()) {
				materials.add(defaultMaterial);
			}

			materials.add(material);
		}

		this.meshes = meshes;
		this.materials = materials;
		this.skeletons = skeletons;
		return this;
	}

	public List<Material> getMaterials() {
		return materials;
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}

	public Map<String, Skeleton> getSkeletonsMap() {
		return skeletons;
	}

	public Skeleton getSkeleton(String name) {
		return skeletons.get(name);
	}

	public List<Bone> getBones() {
		return new ArrayList<>(skeletons.values()).stream().flatMap(skeleton -> skeleton.getBoneMap().values().stream()).toList();
	}

	public Bone findBone(String name) {
		for (Skeleton skeleton : skeletons.values()) {
			Bone bone = skeleton.getBone(name);
			if (bone != null)
				return bone;
		}
    	return null;
	}

	public static class Material {
		private final String name;
		private final List<Mesh> meshList;

		public Material(String name) {
			this.name = name;
			this.meshList = new ArrayList<>();
		}

		public static Material process(AIMaterial aiMaterial) {
			AIString materialName = AIString.create();
			Assimp.aiGetMaterialString(aiMaterial, Assimp.AI_MATKEY_NAME, Assimp.aiTextureType_NONE, 0, materialName);
			String materialNameStr = materialName.dataString();

			return new Material(materialNameStr);
    	}

		public String getName() {
			return name;
		}

		public List<Mesh> getMeshList() {
			return meshList;
		}
	}

	public static class Mesh {
		private final int[] indices;
		private final float[] vertices;
		private final float[] uvs;
		private final float[] normals;
		private final VertexWeight[] vertexWeights;

		public Mesh(int[] indices, float[] vertices, float[] uvs, float[] normals, VertexWeight[] vertexWeights) {
			this.indices = indices;
			this.vertices = vertices;
			this.uvs = uvs;
			this.normals = normals;
			this.vertexWeights = vertexWeights;
		}

		public static Mesh process(AIMesh aiMesh, Map<String, Skeleton> skeletons) {
			int[] indices = processIndices(aiMesh);
			float[] vertices = processVertices(aiMesh);
			float[] uvs = processUVCoords(aiMesh);
			float[] normals = processNormals(aiMesh);
			VertexWeight[] vertexWeights = processBones(aiMesh, skeletons);

			return new Mesh(indices, vertices, uvs, normals, vertexWeights);
		}

		private static int[] processIndices(AIMesh aiMesh) {
			List<Integer> indices = new ArrayList<>();
			for (AIFace face : aiMesh.mFaces()) {
				for (int i = 0; i < face.mNumIndices(); i++) {
					indices.add(face.mIndices().get(i));
				}
			}
			return indices.stream().mapToInt(Integer::intValue).toArray();
		}

		private static float[] processVertices(AIMesh aiMesh) {
			AIVector3D.Buffer buffer = aiMesh.mVertices();
			float[] data = new float[buffer.remaining() * 3];
			int pos = 0;
			while (buffer.remaining() > 0) {
				AIVector3D vector = buffer.get();
				data[pos++] = vector.x();
            	data[pos++] = vector.y();
            	data[pos++] = vector.z();
			}
			return data;
		}

		private static float[] processUVCoords(AIMesh aiMesh) {
			AIVector3D.Buffer buffer = aiMesh.mTextureCoords(0);
			float[] data = new float[buffer.remaining() * 2];
			int pos = 0;
			while (buffer.remaining() > 0) {
				AIVector3D vector = buffer.get();
				data[pos++] = vector.x();
				data[pos++] = 1 - vector.y();
			}
			return data;
		}

		private static float[] processNormals(AIMesh aiMesh) {
			AIVector3D.Buffer buffer = aiMesh.mNormals();
			float[] data = new float[buffer.remaining() * 3];
			int pos = 0;
			while (buffer.remaining() > 0) {
				AIVector3D vector = buffer.get();
				data[pos++] = vector.x();
				data[pos++] = vector.y();
				data[pos++] = vector.z();
			}
			return data;
		}

		private static VertexWeight[] processBones(AIMesh aiMesh, Map<String, Skeleton> skeletons) {
			List<VertexWeight> vertexWeights = new ArrayList<>();
			for (int i = 0; i < aiMesh.mNumBones(); i++) {
				AIBone aiBone = AIBone.create(aiMesh.mBones().get(i));
				Bone bone = Bone.process(aiBone, skeletons);

				for (int i1 = 0; i1 < aiBone.mNumWeights(); i1++) {
					AIVertexWeight aiVertexWeight = aiBone.mWeights().get(i1);

					vertexWeights.add(new VertexWeight(bone, aiVertexWeight.mVertexId(), aiVertexWeight.mWeight()));
				}
			}

			return vertexWeights.toArray(VertexWeight[]::new);
		}

		private void render(VertexConsumer vertexConsumer, PoseStack poseStack, int overlayUV, int lightmapUV) {
			Vector3f transformedPosition = new Vector3f();

			Matrix3f emptyMatrix = new Matrix3f();
			Matrix3f normalOffsetMatrix = new Matrix3f();
			Matrix3f normalMatrix = new Matrix3f();
			Vector3f transformedNormal = new Vector3f();

			for (Integer i : this.indices) {
				float posX = this.vertices[i * 3];
				float posY = this.vertices[i * 3 + 1];
				float posZ = this.vertices[i * 3 + 2];

				float u = this.uvs[i * 2];
				float v = this.uvs[i * 2 + 1];

				float normX = this.normals[i * 3];
				float normY = this.normals[i * 3 + 1];
				float normZ = this.normals[i * 3 + 2];

				// TODO Find a way to skin the mesh on the GPU instead of the CPU
				transformedNormal.set(normX, normY, normZ);
				if (vertexWeights.length != 0) {
					float finalPosX = 0;
					float finalPosY = 0;
					float finalPosZ = 0;

					for (VertexWeight vertexWeight : vertexWeights) {
						if (vertexWeight.vertex() != i) continue;
						Bone bone = vertexWeight.bone();
						float weightValue = vertexWeight.value();

						Matrix4fc globalMatrix = bone.getGlobalTransform();
						Matrix4fc offsetMatrix = bone.getInitialTransform();

						transformedPosition.set(posX, posY, posZ);
						bone.getInitialTransform().transformPosition(transformedPosition);
						globalMatrix.transformPosition(transformedPosition);
						transformedPosition.mul(weightValue);

						finalPosX += transformedPosition.x;
						finalPosY += transformedPosition.y;
						finalPosZ += transformedPosition.z;

						normalOffsetMatrix.set(offsetMatrix);
						normalMatrix.set(globalMatrix);
						normalMatrix.mul(new Matrix3f(offsetMatrix));
						emptyMatrix.lerp(normalMatrix, weightValue, normalMatrix);
						normalMatrix.transform(transformedNormal);
					}
					posX = finalPosX;
					posY = finalPosY;
					posZ = finalPosZ;

					normX = transformedNormal.x;
					normY = transformedNormal.y;
					normZ = transformedNormal.z;
				} else {
					float finalPosY = posZ;
					float finalPosZ = -posY;

					float finalNormY = normZ;
					float finalNormZ = -normY;

					posY = finalPosY;
					posZ = finalPosZ;

					normY = finalNormY;
					normZ = finalNormZ;
				}

				vertexConsumer
						.addVertex(poseStack.last(), posX, posY, posZ)
						.setColor(255, 255, 255, 255)
						.setUv(u, v)
						.setOverlay(overlayUV)
						.setLight(lightmapUV)
						.setNormal(poseStack.last(), normX, normY, normZ);
			}
		}
	}

	public static class Skeleton extends Bone {
		private Map<String, Bone> bones = new HashMap<>();

		public Skeleton(String name, Matrix4f transformation) {
			//Todo Scuffed fix for the 100 scaling on armature bones
			super(0, name, transformation, null);
			this.bones.put(name, this);
		}

		public Map<String, Bone> getBoneMap() {
			return bones;
		}

		public Bone getBone(String name) {
			return bones.get(name);
		}
	}

	public static class Bone {
		private final int id;
		private final String name;
		private final Bone parent;
		private List<Bone> children = new ArrayList<>();

		private final Matrix4f initialTransform;
		private final Matrix4f localTransform;
		private final Matrix4f globalTransform;

		public Bone(int id, String name, Matrix4f transformation, Bone parent) {
			this.id = id;
			this.name = name;
			this.parent = parent;
			this.initialTransform = transformation;
			this.localTransform = new Matrix4f();
			this.globalTransform = new Matrix4f();

			if (parent != null)
				parent.children.add(this);
		}

		public static Bone process(AIBone aiBone, Map<String, Skeleton> skeletons) {
			AINode armature = aiBone.mArmature();
			String armatureName = armature.mName().dataString();
			Skeleton skeleton = skeletons.computeIfAbsent(armatureName, k -> new Skeleton(k, new Matrix4f().identity()));

			String name = aiBone.mName().dataString();
			Bone parent = skeleton.getBone(aiBone.mNode().mParent().mName().dataString());
			Bone bone = new Bone(skeleton.bones.size(), name, AssimpUtils.toMatrix4f(aiBone.mOffsetMatrix()), parent);
			skeleton.bones.put(name, bone);
			return bone;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}

		public Bone getParent() {
			return parent;
		}

		public List<Bone> getChildren() {
			return children;
		}

		public void updateGlobalTransform() {
			if (parent != null) {
				parent.getGlobalTransform().mul(localTransform, globalTransform);
			} else {
				globalTransform.set(localTransform);
			}

			children.forEach(Bone::updateGlobalTransform);
		}

		public Matrix4f getInitialTransform() {
			return initialTransform;
		}

		public Matrix4f getLocalTransform() {
			return localTransform;
		}

		public Matrix4f getGlobalTransform() {
			return globalTransform;
		}
	}

	public record VertexWeight(Bone bone, int vertex, float value) {}
}

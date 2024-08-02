/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.resource.loader;

import me.pandamods.pandalib.resource.model.Mesh;
import me.pandamods.pandalib.resource.model.Model;
import me.pandamods.pandalib.resource.model.Node;
import me.pandamods.pandalib.utils.AssimpUtils;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModelLoader {
	public static Model loadScene(Model model, AIScene scene) {
		List<Mesh> meshes = new ArrayList<>();
		List<String> materials = new ArrayList<>();
		List<Node> nodes = new ArrayList<>();

		// Build Node Tree
		Node rootNode = buildNodeTree(scene.mRootNode(), null, nodes);

		// Process Materials
		for (int i = 0; i < scene.mNumMaterials(); i++) {
			AIMaterial material = AIMaterial.create(scene.mMaterials().get(i));
			AIString materialName = AIString.create();
			Assimp.aiGetMaterialString(material, Assimp.AI_MATKEY_NAME, Assimp.aiTextureType_NONE, 0, materialName);
			String materialNameStr = materialName.dataString();
			materials.add(materialNameStr);
		}

		// Process Meshes
		for (int i = 0; i < scene.mNumMeshes(); i++) {
			AIMesh aiMesh = AIMesh.create(scene.mMeshes().get(i));
			Mesh mesh = processMesh(aiMesh, materials, nodes);
			meshes.add(mesh);
		}

		return model.set(rootNode, meshes, nodes);
	}

	public static Node buildNodeTree(AINode aiNode, Node parent, List<Node> nodes) {
		String name = aiNode.mName().dataString();
		Node node = new Node(name, AssimpUtils.toMatrix4f(aiNode.mTransformation()), parent);
		nodes.add(node);

		for (int i = 0; i < aiNode.mNumMeshes(); i++) {
			node.getMeshIndexes().add(aiNode.mMeshes().get(i));
		}

		for (int i = 0; i < aiNode.mNumChildren(); i++) {
			AINode childNode = AINode.create(aiNode.mChildren().get(i));
			buildNodeTree(childNode, node, nodes);
		}
		return node;
	}

	// Mesh Processing Section
	public static Mesh processMesh(AIMesh aiMesh, List<String> materials, List<Node> nodes) {
		int[] indices = processIndices(aiMesh);
		float[] vertices = processVertices(aiMesh);
		float[] uvs = processUVCoords(aiMesh);
		float[] normals = processNormals(aiMesh);
		int[] boneIndices = new int[vertices.length / 3 * 4];
		float[] boneWeights = new float[vertices.length / 3 * 4];

		processBones(aiMesh, nodes, boneIndices, boneWeights);

		return new Mesh(indices, vertices, uvs, normals, boneIndices, boneWeights, materials.get(aiMesh.mMaterialIndex()));
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

	private static void processBones(AIMesh aiMesh, List<Node> nodes, int[] boneIndices, float[] boneWeights) {
		Arrays.fill(boneIndices, -1);
		Arrays.fill(boneWeights, 0.0f);

		for (int i = 0; i < aiMesh.mNumBones(); i++) {
			AIBone aiBone = AIBone.create(aiMesh.mBones().get(i));
			Node boneNode = findNode(aiBone.mNode(), nodes);

			for (int j = 0; j < aiBone.mNumWeights(); j++) {
				AIVertexWeight aiVertexWeight = aiBone.mWeights().get(j);
				int vertexId = aiVertexWeight.mVertexId();
				float weight = aiVertexWeight.mWeight();

				for (int k = 0; k < 4; k++) {
					if (boneWeights[vertexId * 4 + k] == 0) {
						boneIndices[vertexId * 4 + k] = nodes.indexOf(boneNode);
						boneWeights[vertexId * 4 + k] = weight;
						break;
					}
				}
			}
		}
	}

	private static Node findNode(AINode aiNode, List<Node> nodes) {
		for (Node node : nodes) {
			if (AssimpUtils.AINodeEqualsNode(aiNode, node)) {
				return node;
			}
		}
		return null;
	}
}

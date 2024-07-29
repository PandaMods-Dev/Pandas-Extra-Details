package me.pandamods.pandalib.resource.model;

public class Mesh {
	private final int[] indices;
	private final float[] vertices;
	private final float[] uvs;
	private final float[] normals;
	private final int[] boneIndices;
	private final float[] boneWeights;
	private final String materialName;

	public Mesh(int[] indices, float[] vertices, float[] uvs, float[] normals, int[] boneIndices, float[] boneWeights, String materialName) {
		this.indices = indices;
		this.vertices = vertices;
		this.uvs = uvs;
		this.normals = normals;
		this.boneIndices = boneIndices;
		this.boneWeights = boneWeights;
		this.materialName = materialName;
	}

	public int[] getIndices() {
		return indices;
	}

	public float[] getVertices() {
		return vertices;
	}

	public float[] getUvs() {
		return uvs;
	}

	public float[] getNormals() {
		return normals;
	}

	public int[] getBoneIndices() {
		return boneIndices;
	}

	public float[] getBoneWeights() {
		return boneWeights;
	}

	public String getMaterialName() {
		return materialName;
	}
}

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

package me.pandamods.pandalib.resource;

import com.mojang.blaze3d.vertex.*;
import me.pandamods.pandalib.client.render.PLRenderTypes;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
	private final int numVertices;
	private final Float[] vertices;
	private final Float[] uvs;
	private final Float[] normals;

	public Mesh(AIScene scene) {
		int numVertices = 0;
		List<Float> vertices = new ArrayList<>();
		List<Float> uvs = new ArrayList<>();
		List<Float> normals = new ArrayList<>();

		PointerBuffer buffer = scene.mMeshes();
		for (int i = 0; i < buffer.limit(); i++) {
			AIMesh mesh = AIMesh.create(buffer.get(i));

			numVertices += mesh.mNumVertices();

			for (AIVector3D position : mesh.mVertices()) {
				vertices.add(position.x());
				vertices.add(position.y());
				vertices.add(position.z());
			}

			for (AIVector3D uv : mesh.mTextureCoords(0)) {
				uvs.add(uv.x());
				uvs.add(uv.y());
			}

			for (AIVector3D normal : mesh.mNormals()) {
				normals.add(normal.x());
				normals.add(normal.y());
				normals.add(normal.z());
			}
		}

		this.numVertices = numVertices;
		this.vertices = vertices.toArray(new Float[0]);
		this.uvs = uvs.toArray(new Float[0]);
		this.normals = normals.toArray(new Float[0]);
	}

	public void render(MultiBufferSource bufferSource, Matrix4f worldMatrix, Matrix3f normalMatrix, int overlayUV, int lightmapUV) {
		VertexConsumer vertexConsumer = bufferSource.getBuffer(PLRenderTypes.RENDERTYPE_TRIANGULAR);

		for (int i = 0; i < this.numVertices; i++) {
			float posX = this.vertices[i * 3];
			float posY = this.vertices[i * 3 + 1];
			float posZ = this.vertices[i * 3 + 2];

			float u = this.uvs[i * 2];
			float v = this.uvs[i * 2 + 1];

			float normX = this.normals[i * 3];
			float normY = this.normals[i * 3 + 1];
			float normZ = this.normals[i * 3 + 2];

			vertexConsumer
					.vertex(worldMatrix, posX, posY, posZ)
					.color(1f, 1f, 1f, 1f)
					.uv(u, v)
					.overlayCoords(overlayUV)
					.uv2(lightmapUV)
					.normal(normalMatrix, normX, normY, normZ)
					.endVertex();
		}
	}
}

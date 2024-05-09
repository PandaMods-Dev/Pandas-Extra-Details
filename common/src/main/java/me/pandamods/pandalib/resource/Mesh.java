package me.pandamods.pandalib.resource;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.utils.PLSpriteCoordinateExpander;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Mesh {
	private final Object[] objects;

	public Mesh(AIScene scene) {
		List<Object> objects = new ArrayList<>();

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

	public void render(Matrix4f worldMatrix, Matrix3f normalMatrix, int overlayUV, int lightmapUV,
					   Function<String, VertexConsumer> vertexConsumerProvider) {
		for (Object object : this.objects) {
			object.render(vertexConsumerProvider.apply(object.materialName), worldMatrix, normalMatrix, overlayUV, lightmapUV);
		}
	}

	public static class Object {
		private final Integer[] indices;
		private final Float[] vertices;
		private final Float[] uvs;
		private final Float[] normals;
		private final String materialName;

		public Object(AIMesh mesh, String materialName) {
			List<Integer> indices = new ArrayList<>();
			List<Float> vertices = new ArrayList<>();
			List<Float> uvs = new ArrayList<>();
			List<Float> normals = new ArrayList<>();

			for (AIFace face : mesh.mFaces()) {
				for (int i1 = 0; i1 < face.mNumIndices(); i1++) {
					indices.add(face.mIndices().get(i1));
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

			this.indices = indices.toArray(new Integer[0]);
			this.vertices = vertices.toArray(new Float[0]);
			this.uvs = uvs.toArray(new Float[0]);
			this.normals = normals.toArray(new Float[0]);
			this.materialName = materialName;
		}

		private void render(VertexConsumer vertexConsumer, Matrix4f worldMatrix, Matrix3f normalMatrix, int overlayUV, int lightmapUV) {
			for (Integer i : this.indices) {
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
						.color(255, 255, 255, 255)
						.uv(u, v)
						.overlayCoords(overlayUV)
						.uv2(lightmapUV)
						.normal(normalMatrix, normX, normY, normZ)
						.endVertex();
			}
		}
	}
}

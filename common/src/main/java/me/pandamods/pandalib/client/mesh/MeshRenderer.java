package me.pandamods.pandalib.client.mesh;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.client.Model;
import me.pandamods.pandalib.client.armature.Armature;
import me.pandamods.pandalib.client.armature.Bone;
import me.pandamods.pandalib.resource.MeshData;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MeshRenderer<T, M extends Model<T>> {
	M getModel();

	default void renderGeometry(T t, Armature armature, PoseStack poseStack, MultiBufferSource bufferSource,
								int lightColor, int overlayTexture) {
		M model = getModel();
		MeshData meshData = ExtraDetails.RESOURCES.meshes.get(model.modelLocation(t));
		Color color = Color.white;

		Map<String, ResourceLocation> textures = model.textureLocation(t);
		Map<String, VertexConsumer> consumers = new HashMap<>();

		for (MeshData.Object object : meshData.objects().values()) {
			List<Vertex> vertices = object.vertices().stream().map(vertex -> {
				Vector3f position = new Vector3f(vertex.position()).rotate(object.rotation()).add(object.position());
				Quaternionf normalRotation = new Quaternionf().identity();
				if (armature != null && !vertex.weights().isEmpty()) {
					Vector3f finalPosition = new Vector3f();
					Quaternionf finalNormalRotation = new Quaternionf().identity();
					for (MeshData.Weight weight : vertex.weights()) {
						Optional<Bone> boneOptional = armature.getBone(weight.name());
						float weightPercent = weight.weight() / vertex.max_weight();

						if (boneOptional.isPresent()) {
							Bone bone = boneOptional.get();
							Matrix4f initialTransform = new Matrix4f(bone.initialTransform);
							Matrix4f globalTransform = new Matrix4f(bone.getGlobalTransform());
							globalTransform.mul(initialTransform.invert(new Matrix4f()));

							Vector3f transformedPosition = new Vector3f(position);
							globalTransform.transformPosition(transformedPosition);

							transformedPosition.mul(weightPercent);
							finalPosition.add(transformedPosition);

							Quaternionf transformedNormalRotation = new Quaternionf(normalRotation);
							globalTransform.rotate(transformedNormalRotation);

							transformedNormalRotation.mul(weightPercent);
							finalNormalRotation.mul(transformedNormalRotation);
						}
					}

					position.set(finalPosition);
					normalRotation.set(finalNormalRotation);
				}
				return new Vertex(vertex.index(), position, normalRotation);
			}).toList();

			for (MeshData.Face face : object.faces()) {
				VertexConsumer vertexConsumer = consumers.get(face.texture_name());
				if (vertexConsumer == null) {
					vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(
							textures.getOrDefault(face.texture_name(), textures.get(""))
					));
					consumers.put(face.texture_name(), vertexConsumer);
				}
				for (Integer vertexIndex : face.vertices()) {
					Vertex vertex = vertices.get(vertexIndex);
					Vector3f normal = new Vector3f(face.normal()).rotate(vertex.normalRotation).normalize();
					vertex(vertexConsumer, poseStack.last(),
							vertex.position(), normal, new Vector2f(face.vertex_uvs().get(vertexIndex)),
							color, lightColor, overlayTexture
					);
				}
			}
		}
	}

	static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose,
							   Vector3f position, Vector3f normal, Vector2f uv, Color color, int lightColor, int overlayTexture) {
		vertexConsumer
				.vertex(pose.pose(), position.x, position.y, position.z)
				.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
				.uv(uv.x, 1 - uv.y)
				.overlayCoords(overlayTexture)
				.uv2(lightColor)
				.normal(pose.normal(), normal.x, normal.y, normal.z)
				.endVertex();
	}

	record Vertex(
			int index, Vector3f position,
			Quaternionf normalRotation) {}
}

package me.pandamods.pandalib.client.mesh;

import com.mojang.blaze3d.vertex.*;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.client.Model;
import me.pandamods.pandalib.client.armature.Armature;
import me.pandamods.pandalib.client.armature.Bone;
import me.pandamods.pandalib.client.armature.IAnimatableCache;
import me.pandamods.pandalib.resource.ArmatureData;
import me.pandamods.pandalib.resource.MeshData;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.*;

import java.awt.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface MeshRenderer<T, M extends Model<T>> {
	M getModel();

	default boolean debug() {
		return true;
	}

	default void renderGeometry(T t, Armature armature, PoseStack poseStack, MultiBufferSource bufferSource, int lightColor, int overlayTexture) {
		M model = getModel();
		MeshData meshData = ExtraDetails.RESOURCES.meshes.get(model.modelLocation(t));
		Color color = Color.white;

		for (MeshData.Object object : meshData.objects().values()) {
			poseStack.pushPose();
//			poseStack.translate(objectPosition.x, objectPosition.y, objectPosition.z);
//			poseStack.mulPose(new Quaternionf().identity().rotateZYX(objectRotation.z, objectRotation.y, objectRotation.x));

			List<Vertex> vertices = object.vertices().stream().map(vertex -> {
				Vector3f position = new Vector3f(vertex.position());

				if (armature != null && !vertex.weights().isEmpty()) {
					for (MeshData.Weight weight : vertex.weights()) {
						Optional<Bone> boneOptional = armature.getBone(weight.name());
						float weightPercent = weight.weight() / vertex.max_weight();

						if (boneOptional.isPresent()) {
							Bone bone = boneOptional.get();
							Matrix4f initialTransform = new Matrix4f(bone.getInitialTransform());
							Matrix4f globalTransform = new Matrix4f(bone.getGlobalTransform());
							Vector3f initialPosition = new Vector3f(position);

							globalTransform.mul(initialTransform.invert(new Matrix4f()));
							globalTransform.transformProject(position);

							Vector3f positionDifference = new Vector3f(position).sub(initialPosition);
							positionDifference.mul(weightPercent);
							position.set(initialPosition.add(positionDifference));
						}
					}
				}
				return new Vertex(vertex.index(), position);
			}).toList();

			for (MeshData.Face face : object.faces()) {
				VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(getTexture(t, face.texture_name())));
				for (Integer vertexIndex : face.vertices()) {
					Vertex vertex = vertices.get(vertexIndex);
					vertex(vertexConsumer, poseStack.last(),
							vertex.position(), new Vector3f(face.normal()), new Vector2f(face.vertex_uvs().get(vertexIndex)),
							color, lightColor, overlayTexture
					);
				}
			}

			poseStack.popPose();
		}
	}

	default ResourceLocation getTexture(T t, String name) {
		if (getModel().textureLocation(t).containsKey(name))
			return getModel().textureLocation(t).get(name);
		return getModel().textureLocation(t).values().stream().findFirst().orElse(new ResourceLocation(""));
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
			int index, Vector3f position
	) {}
}

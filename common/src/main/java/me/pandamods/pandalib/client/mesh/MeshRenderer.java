package me.pandamods.pandalib.client.mesh;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.client.Model;
import me.pandamods.pandalib.resource.MeshData;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.awt.*;
import java.util.List;

public interface MeshRenderer<T> {
	Model<T> getModel();

	default boolean debug() {
		return true;
	}

	default void renderGeometry(T t, PoseStack poseStack, MultiBufferSource bufferSource, int lightColor, int overlayTexture) {
		Model<T> model = getModel();
		MeshData meshData = ExtraDetails.RESOURCES.meshes.get(model.modelLocation(t));
		Color color = Color.white;

		for (MeshData.Object object : meshData.objects().values()) {
			float[] objectPosition = object.position();
			float[] objectRotation = object.position();
			poseStack.pushPose();
			poseStack.translate(objectPosition[0], objectPosition[1], objectPosition[2]);
			poseStack.mulPose(new Quaternionf().identity().rotateZYX(objectRotation[0], objectRotation[1], objectRotation[2]));

			List<Vertex> vertices = object.vertices().stream().map(vertex -> new Vertex(new Vector3f(vertex.position()))).toList();

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

	private static void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose,
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
			Vector3f position
	) {}
}

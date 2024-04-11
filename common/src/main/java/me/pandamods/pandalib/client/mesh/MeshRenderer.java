package me.pandamods.pandalib.client.mesh;

import com.mojang.blaze3d.vertex.*;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.client.Model;
import me.pandamods.pandalib.client.armature.Armature;
import me.pandamods.pandalib.client.armature.Bone;
import me.pandamods.pandalib.resource.MeshData;
import me.pandamods.pandalib.resource.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.TextureAtlasHolder;
import net.minecraft.resources.ResourceLocation;
import org.joml.*;

import java.awt.*;
import java.util.*;
import java.util.List;

public interface MeshRenderer<T, M extends Model<T>> {
	Color DEFAULT_COLOR = Color.white;

	M getModel();

	default void renderGeometry(T t, Armature armature, PoseStack poseStack, MultiBufferSource bufferSource, int lightColor) {
		M model = getModel();
		MeshData meshData = Resources.getMesh(model.modelLocation(t));
		Color color = getColor(t);
		int overlayTexture = getOverlayTexture(t);
		VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entitySolid(new ResourceLocation("textures/block/cobblestone.png")));

		List<Vertex> vertices = new ArrayList<>();
		for (MeshData.Vertex vertex : meshData.vertices()) {
			Vector3f position = new Vector3f(vertex.position());
			Quaternionf normalRotation = new Quaternionf().identity();
			if (armature != null && !vertex.weights().isEmpty()) {
				Vector3f finalPosition = new Vector3f();
				Quaternionf finalNormalRotation = new Quaternionf().identity();
				for (MeshData.Weight weight : vertex.weights()) {
					Bone bone = armature.getBone(weight.name());
					float weightPercent = weight.weight() / vertex.max_weight();

					if (bone != null) {
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
			vertices.add(new Vertex(position, normalRotation));
		}

		Vector3f fullVertexPosition = new Vector3f();
		Vector3f fullVertexNormal = new Vector3f();

		for (MeshData.Face face : meshData.faces()) {
//			VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(model.textureLocation(t, face.texture_name())));
			for (MeshData.FaceVertex faceVertex : face.vertices()) {
				Vertex vertex = vertices.get(faceVertex.index());
				PoseStack.Pose pose = poseStack.last();

				pose.pose().transformPosition(vertex.position, fullVertexPosition);
				face.normal().rotate(vertex.normalRotation, fullVertexNormal);
				pose.normal().transform(fullVertexNormal);
				vertexConsumer
						.vertex(fullVertexPosition.x, fullVertexPosition.y, fullVertexPosition.z)
						.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
						.uv(faceVertex.uv().x(), 1 - faceVertex.uv().y())
						.overlayCoords(overlayTexture)
						.uv2(lightColor)
						.normal(fullVertexNormal.x, fullVertexNormal.y, fullVertexNormal.z)
						.endVertex();
			}
		}
	}

	default Color getColor(T t) {
		return DEFAULT_COLOR;
	}

	default int getOverlayTexture(T t) {
		return OverlayTexture.NO_OVERLAY;
	}

	record Vertex(Vector3f position, Quaternionf normalRotation) {}
}

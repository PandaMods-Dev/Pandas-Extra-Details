package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.Bone;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.entity.MeshAnimatable;
import me.pandamods.pandalib.resources.Mesh;
import me.pandamods.pandalib.resources.Resources;
import me.pandamods.pandalib.utils.GameUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.*;

import java.awt.*;
import java.lang.Math;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public abstract class MeshRenderer<T extends MeshAnimatable, M extends MeshModel<T>> {
	public final M model;

	public MeshRenderer(M model) {
		this.model = model;
	}

	public RenderType getRenderType(ResourceLocation location) {
		return RenderType.entityCutout(location);
	}

	public void renderMesh(T entity, PoseStack stack, MultiBufferSource buffer, float partialTick, int packedLight, int packedOverlay) {
		stack.pushPose();
//		stack.mulPose(Axis.XP.rotationDegrees(-90));

		Mesh mesh = Resources.meshes.getOrDefault(this.model.getMeshLocation(entity), null);
		if (mesh != null) {
			if (entity.getCache().mesh == null || !entity.getCache().mesh.equals(mesh)) {
				entity.getCache().mesh = mesh;
				entity.getCache().armature = new Armature(entity.getCache().mesh);
			}

			if (entity.getCache().mesh != null) {
				if (entity.getCache().armature != null) {
					float deltaSeconds = GameUtils.getDeltaSeconds();
					this.model.setupAnim(entity, entity.getCache().armature, deltaSeconds);
				}

				if (Objects.equals(entity.getCache().mesh.format_version(), "0.1")) {
					for (Map.Entry<String, Mesh.Object> meshEntry : entity.getCache().mesh.mesh().entrySet()) {
						renderObject(meshEntry.getValue(), entity, stack, buffer, packedLight, packedOverlay);
					}
				}
			}
		} else {
			ExtraDetails.LOGGER.error("Cant find mesh " + this.model.getMeshLocation(entity));
		}
		stack.popPose();
	}

	public void renderObject(Mesh.Object object, T entity, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		stack.pushPose();
		for (Mesh.Object.Face face : object.faces()) {
			VertexConsumer consumer = buffer.getBuffer(this.getRenderType(this.model.getTextureLocation(face.texture_name(), entity)));

			for (Mesh.Object.Face.Vertex vertex : face.vertices()) {
				Matrix4f pose = stack.last().pose();
				Matrix3f normal = stack.last().normal();

				// important part
				float maxWeight = (float) Arrays.stream(vertex.weights()).mapToDouble(Mesh.Object.Face.Vertex.Weight::weight).sum();
				Vector3f vertexPos = new Vector3f(vertex.position()).add(object.position());
				Vector3f newVertexPos = new Vector3f();
				Vector3f vertexNormal = new Vector3f(face.normal());

				if (vertex.weights().length > 0) {
					for (Mesh.Object.Face.Vertex.Weight weight : vertex.weights()) {
						Optional<Bone> bone = entity.getCache().armature.getBone(weight.name());
						float weightPercent = weight.weight() / maxWeight;

						if (bone.isPresent()) {
							Bone boneInst = bone.get();

							Matrix4f boneTransform = boneInst.getWorldTransform();

							Vector4f transformedPosition = new Vector4f(vertexPos, 1.0f);
							boneTransform.transform(transformedPosition);

							newVertexPos.add(new Vector3f(transformedPosition.x, transformedPosition.y, transformedPosition.z).mul(weightPercent));
						}
					}
				} else {
					newVertexPos.set(vertexPos);
				}

				// creating the vertex
				this.vertex(pose, normal, consumer, Color.WHITE,
						newVertexPos, new Vector2f(vertex.uv()[0],  1 - vertex.uv()[1]), vertexNormal,
						packedLight, packedOverlay);
				// important part end
			}
		}
		stack.popPose();
	}

	public final void vertex(Matrix4f pose, Matrix3f normal, VertexConsumer vertexConsumer, Color color,
							 Vector3f pos, Vector2f uv, Vector3f normalVec, int packedLight, int packedOverlay) {
		vertexConsumer.vertex(pose, pos.x, pos.y, pos.z)
				.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
				.uv(uv.x, uv.y)
				.overlayCoords(packedOverlay)
				.uv2(packedLight)
				.normal(normal, normalVec.x, normalVec.y, normalVec.z)
				.endVertex();
	}
}

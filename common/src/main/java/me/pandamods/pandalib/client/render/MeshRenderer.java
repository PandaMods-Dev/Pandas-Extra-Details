package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.Bone;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.entity.MeshAnimatable;
import me.pandamods.pandalib.resources.Mesh;
import me.pandamods.pandalib.resources.Resources;
import me.pandamods.pandalib.utils.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.*;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public interface MeshRenderer<T extends MeshAnimatable, M extends MeshModel<T>> {

	default RenderType getRenderType(ResourceLocation location) {
		return RenderType.entityCutout(location);
	}

	default void renderMesh(T base, M model, PoseStack stack, MultiBufferSource buffer, float partialTick, int packedLight, int packedOverlay) {
		stack.pushPose();

		Mesh mesh = Resources.MESHES.getOrDefault(model.getMeshLocation(base), null);
		if (mesh != null) {
			if (base.getCache().mesh == null || !base.getCache().mesh.equals(mesh)) {
				base.getCache().mesh = mesh;
				base.getCache().armature = new Armature(base.getCache().mesh);
			}

			if (base.getCache().mesh != null) {
				if (base.getCache().armature != null) {
					float deltaSeconds = RenderUtils.getDeltaSeconds();
					model.setupAnim(base, base.getCache().armature, deltaSeconds);
				}

				if (Objects.equals(base.getCache().mesh.format_version(), "0.1")) {
					for (Map.Entry<String, Mesh.Object> meshEntry : base.getCache().mesh.mesh().entrySet()) {
						renderObject(meshEntry.getValue(), base, model, stack, buffer, packedLight, packedOverlay);
					}
				}
			}
		} else {
			ExtraDetails.LOGGER.error("Cant find mesh " + model.getMeshLocation(base));
		}
		stack.popPose();
	}

	default void renderObject(Mesh.Object object, T base, M model, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		stack.pushPose();
		for (Mesh.Object.Face face : object.faces()) {
			VertexConsumer consumer = buffer.getBuffer(this.getRenderType(model.getTextureLocation(face.texture_name(), base)));

			for (Mesh.Object.Face.Vertex vertex : face.vertices()) {
				Matrix4f pose = stack.last().pose();
				Matrix3f normal = stack.last().normal();

				float maxWeight = (float) Arrays.stream(vertex.weights()).mapToDouble(Mesh.Object.Face.Vertex.Weight::weight).sum();
				Vector3f vertexPos = new Vector3f(vertex.position()).add(object.position());
				Vector3f newVertexPos = new Vector3f();
				Vector3f vertexNormal = new Vector3f(face.normal());

				if (vertex.weights().length > 0) {
					for (Mesh.Object.Face.Vertex.Weight weight : vertex.weights()) {
						Optional<Bone> bone = base.getCache().armature.getBone(weight.name());
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

				this.vertex(pose, normal, consumer, Color.WHITE,
						newVertexPos, new Vector2f(vertex.uv()[0],  1 - vertex.uv()[1]), vertexNormal,
						packedLight, packedOverlay);
			}
		}
		stack.popPose();
	}

	default void vertex(Matrix4f pose, Matrix3f normal, VertexConsumer vertexConsumer, Color color,
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

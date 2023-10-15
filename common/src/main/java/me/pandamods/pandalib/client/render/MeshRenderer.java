package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.cache.MeshCache;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.Bone;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.entity.MeshAnimatable;
import me.pandamods.pandalib.resources.Mesh;
import me.pandamods.pandalib.resources.Resources;
import me.pandamods.pandalib.utils.RenderUtils;
import me.pandamods.pandalib.utils.time_testing.TimeTesting;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.joml.*;

import java.awt.*;
import java.util.*;

@Environment(EnvType.CLIENT)
public interface MeshRenderer<T extends MeshAnimatable, M extends MeshModel<T>> {

	default RenderType getRenderType(ResourceLocation location) {
		return RenderType.entityCutout(location);
	}

	default void renderMesh(T base, M model, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		stack.pushPose();
		ResourceLocation location = model.getMeshLocation(base);
		if (base.getCache().mesh == null || !base.getCache().meshLocation.equals(location)) {
			base.getCache().meshLocation = location;
			base.getCache().mesh = Resources.MESHES.getOrDefault(location, null);
			if (base.getCache().mesh != null) {
				base.getCache().armature = new Armature(base.getCache().mesh);
			} else {
				ExtraDetails.LOGGER.error("Cant find objects " + model.getMeshLocation(base));
			}
		}
		Mesh mesh = base.getCache().mesh;
		Armature armature = base.getCache().armature;

		if (mesh != null) {
			if (armature != null) {
				float deltaSeconds = RenderUtils.getDeltaSeconds();
				model.setupAnim(base, armature, deltaSeconds);

				if (Objects.equals(mesh.format_version(), "0.2")) {
					Map<Integer, Map<String, MeshCache.vertexVectors>> vertices = new HashMap<>(base.getCache().vertices);
					base.getCache().vertices.clear();
					for (Map.Entry<String, Mesh.Object> meshEntry : mesh.objects().entrySet()) {
						renderObject(meshEntry.getValue(), base, model, stack, buffer, packedLight, packedOverlay, vertices);
					}
					armature.clearUpdatedBones();
				} else {
					ExtraDetails.LOGGER.error("Format version " + mesh.format_version() + " is not supported");
				}
			}
		}
		stack.popPose();
	}

	default void renderObject(Mesh.Object object, T base, M model, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay,
							  Map<Integer, Map<String, MeshCache.vertexVectors>> vertices) {
		for (Mesh.Object.Face face : object.faces()) {
			VertexConsumer consumer = buffer.getBuffer(this.getRenderType(model.getTextureLocation(face.texture_name(), base)));

			for (Map.Entry<Integer, Mesh.Object.Face.Vertex> vertexEntry : face.vertices().entrySet()) {
				base.getCache().vertices.put(vertexEntry.getKey(), new HashMap<>());
				Mesh.Object.Face.Vertex vertex = vertexEntry.getValue();
				Matrix4f poseMatrix = stack.last().pose();
				Matrix3f normalMatrix = stack.last().normal();

				float maxWeight = (float) Arrays.stream(vertex.weights()).mapToDouble(Mesh.Object.Face.Vertex.Weight::weight).sum();
				Vector3f vertexPos = new Vector3f(vertex.position()).add(object.position());
				Vector3f newVertexPos = new Vector3f();
				Vector3f newVertexNormal = new Vector3f();

				if (vertex.weights().length > 0) {
					for (Mesh.Object.Face.Vertex.Weight weight : vertex.weights()) {
						if (base.getCache().armature.isUpdated(weight.name())) {
							Optional<Bone> bone = base.getCache().armature.getBone(weight.name());
							float weightPercent = weight.weight() / maxWeight;

							if (bone.isPresent()) {
								Bone boneInst = bone.get();

								Matrix4f boneTransform = boneInst.getWorldTransform();

								Vector4f transformedPosition = new Vector4f(vertexPos, 1.0f);
								boneTransform.transform(transformedPosition);

								Vector3f position = new Vector3f(transformedPosition.x, transformedPosition.y, transformedPosition.z).mul(weightPercent);
								newVertexPos.add(position);

								Matrix3f rotationMatrix = new Matrix3f(boneTransform);
								Vector3f transformedNormal = new Vector3f(face.normal());
								rotationMatrix.transform(transformedNormal);

								Vector3f normal = transformedNormal.mul(weightPercent);
								newVertexNormal.add(normal);

								if (!vertices.containsKey(vertexEntry.getKey()))
									base.getCache().vertices.put(vertexEntry.getKey(), new HashMap<>());
								base.getCache().vertices.get(vertexEntry.getKey()).put(weight.name(), new MeshCache.vertexVectors(position, normal));
							}
						} else if (vertices.containsKey(vertexEntry.getKey())) {
							MeshCache.vertexVectors vertexVectors = vertices.get(vertexEntry.getKey()).getOrDefault(weight.name(),
									new MeshCache.vertexVectors(new Vector3f(), new Vector3f()));

							newVertexPos.add(vertexVectors.position());
							newVertexNormal.add(vertexVectors.normal());

							if (!vertices.containsKey(vertexEntry.getKey()))
									base.getCache().vertices.put(vertexEntry.getKey(), new HashMap<>());
							base.getCache().vertices.get(vertexEntry.getKey()).put(weight.name(), vertexVectors);
						}
					}
				} else {
					newVertexPos.set(vertexPos);
					newVertexNormal.set(face.normal());
				}

				this.vertex(poseMatrix, normalMatrix, consumer, Color.WHITE,
						newVertexPos, new Vector2f(vertex.uv()[0],  1 - vertex.uv()[1]), newVertexNormal,
						packedLight, packedOverlay);
			}
		}
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

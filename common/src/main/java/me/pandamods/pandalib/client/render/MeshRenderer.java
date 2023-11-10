package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.cache.MeshCache;
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
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.*;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public interface MeshRenderer<T extends MeshAnimatable, M extends MeshModel<T>> {

	default RenderType getRenderType(ResourceLocation location, T base) {
		return RenderType.entityCutout(location);
	}

	default VertexConsumer getVertexConsumer(MultiBufferSource bufferSource, ResourceLocation location, T base) {
		return bufferSource.getBuffer(getRenderType(location, base));
	}

	default void renderMesh(T base, M model, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		if (base.getCache() != null) {
			stack.pushPose();
			ResourceLocation location = model.getMeshLocation(base);
			if (base.getCache().mesh == null || !base.getCache().meshLocation.equals(location)) {
				base.getCache().meshLocation = location;
				base.getCache().mesh = Resources.MESHES.getOrDefault(location, null);
				if (base.getCache().mesh != null) {
					base.getCache().armature = new Armature(base.getCache().mesh);
				} else {
					PandaLib.LOGGER.error("Cant find mesh at " + model.getMeshLocation(base).toString());
				}
			}
			Mesh mesh = base.getCache().mesh;
			Armature armature = base.getCache().armature;

			if (mesh != null) {
				if (armature != null) {
					float deltaSeconds = RenderUtils.getDeltaSeconds();

					if (base.getCache().animationController == null && model.createAnimationController() != null) {
						base.getCache().animationController = model.createAnimationController().create(base);
					}

					if (base.getCache().animationController != null) {
						base.getCache().animationController.updateAnimations(deltaSeconds);
					}

					model.setupAnim(base, armature, deltaSeconds);

					Map<Integer, Map<String, MeshCache.vertexVectors>> vertices = new HashMap<>(base.getCache().vertices);
					base.getCache().vertices.clear();
					for (Map.Entry<String, Mesh.Object> meshEntry : mesh.objects().entrySet()) {
						renderObject(meshEntry.getValue(), base, model, stack, buffer, packedLight, packedOverlay, vertices);
					}
					armature.clearUpdatedBones();
				}
			}
			stack.popPose();
		}
	}

	default void renderObject(Mesh.Object object, T base, M model, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay,
							  Map<Integer, Map<String, MeshCache.vertexVectors>> vertices) {
		for (Mesh.Object.Face face : object.faces()) {
			ResourceLocation textureLocation = model.getTextureLocation(face.texture_name(), base);
			VertexConsumer consumer = getVertexConsumer(buffer, textureLocation, base);

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

	default void translateBlock(BlockState blockState, PoseStack stack) {
		stack.translate(0.5f, 0.5f, 0.5f);

		float direction = getFacing(blockState).toYRot();
		stack.mulPose(Axis.YP.rotationDegrees(-direction));

		if (blockState.hasProperty(BlockStateProperties.ATTACH_FACE)) {
			AttachFace face = blockState.getValue(BlockStateProperties.ATTACH_FACE);
			switch (face) {
				case CEILING -> stack.mulPose(Axis.XP.rotationDegrees(180));
				case WALL -> stack.mulPose(Axis.XP.rotationDegrees(90));
			}
		}

		stack.translate(0, -0.5f, 0);
	}

	default Direction getFacing(BlockState blockState) {
		if (blockState.hasProperty(HorizontalDirectionalBlock.FACING))
			return blockState.getValue(HorizontalDirectionalBlock.FACING);

		if (blockState.hasProperty(DirectionalBlock.FACING))
			return blockState.getValue(DirectionalBlock.FACING);

		return Direction.NORTH;
	}
}

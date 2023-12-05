package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.pandamods.pandalib.cache.ObjectCache;
import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.Bone;
import me.pandamods.pandalib.client.model.CompiledVertex;
import me.pandamods.pandalib.entity.MeshAnimatable;
import me.pandamods.pandalib.resources.MeshRecord;
import me.pandamods.pandalib.utils.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;
import org.joml.*;

import java.awt.*;
import java.util.List;
import java.util.*;

@Environment(EnvType.CLIENT)
public interface MeshRenderer<T extends MeshAnimatable, M extends me.pandamods.pandalib.client.model.MeshModel<T>> {

	default RenderType getRenderType(ResourceLocation location, T base) {
		return RenderType.entityCutout(location);
	}

	default VertexConsumer getVertexConsumer(MultiBufferSource bufferSource, ResourceLocation location, T base) {
		return bufferSource.getBuffer(getRenderType(location, base));
	}

	default void renderRig(T base, M model, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay,
						   boolean shouldRenderMesh) {
		fullRenderRig(base, model, poseStack, buffer, packedLight, packedOverlay, shouldRenderMesh);
	}

	default void renderRig(T base, M model, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
						   boolean shouldRenderMesh) {
		fullRenderRig(base, model, poseStack, vertexConsumer, packedLight, packedOverlay, shouldRenderMesh);
	}

	private void fullRenderRig(T base, M model, PoseStack poseStack, Object buffer, int packedLight, int packedOverlay,
						   boolean shouldRenderMesh) {
		if (base.getCache() == null) {
			return;
		}

		poseStack.pushPose();
		base.getCache().updateMeshCache(model, base);

		if (shouldRenderMesh) {
			MeshRecord meshRecord = base.getCache().meshRecord;
			Armature armature = base.getCache().armature;

			if (armature != null) {
				animateArmature(base, model, armature);
			}

			if (meshRecord != null) {
				if (buffer instanceof MultiBufferSource) {
					renderMesh(base, model, armature, meshRecord, poseStack, (MultiBufferSource) buffer, packedLight, packedOverlay);
				} else if (buffer instanceof VertexConsumer) {
					renderMesh(base, model, armature, meshRecord, poseStack, (VertexConsumer) buffer, packedLight, packedOverlay);
				}
			}
		}
		poseStack.popPose();
	}

	default void animateArmature(T base, M model, Armature armature) {
		float deltaSeconds = RenderUtils.getDeltaSeconds();

		AnimationControllerProvider<T> animationControllerProvider = model.createAnimationController(base);
		if (base.getCache().animationController == null && animationControllerProvider != null) {
			base.getCache().animationController = animationControllerProvider.create(base);
		}

		if (base.getCache().animationController != null) {
			base.getCache().animationController.updateAnimations(deltaSeconds);
		}

		model.setupAnim(base, armature, deltaSeconds);
	}

	default void renderMesh(T base, M model, @Nullable Armature armature, MeshRecord mesh, PoseStack poseStack,
							MultiBufferSource buffer, int packedLight, int packedOverlay) {
		fullRenderMesh(base, model, armature, mesh, poseStack, buffer, packedLight, packedOverlay);
	}

	default void renderMesh(T base, M model, @Nullable Armature armature, MeshRecord mesh, PoseStack poseStack,
							VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		fullRenderMesh(base, model, armature, mesh, poseStack, vertexConsumer, packedLight, packedOverlay);
	}

	private void fullRenderMesh(T base, M model, @Nullable Armature armature, MeshRecord mesh, PoseStack poseStack,
								Object buffer, int packedLight, int packedOverlay) {
		Map<Integer, Map<String, ObjectCache.vertexVectors>> vertices = new HashMap<>(base.getCache().vertices);
		base.getCache().vertices.clear();

		for (Map.Entry<String, MeshRecord.Object> meshEntry : mesh.objects().entrySet()) {
			if (armature == null || !armature.getVisibility(meshEntry.getKey()))
				if (buffer instanceof MultiBufferSource) {
					renderObject(meshEntry.getValue(), base, model, poseStack, (MultiBufferSource) buffer, packedLight, packedOverlay, Color.WHITE, vertices);
				} else if (buffer instanceof VertexConsumer) {
					renderObject(meshEntry.getValue(), base, model, poseStack, (VertexConsumer) buffer, packedLight, packedOverlay, Color.WHITE, vertices);
				}
		}

		if (armature != null) {
			armature.clearUpdatedBones();
		}
	}

	default void renderObject(MeshRecord.Object object, T base, M model, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay,
							  Color color, Map<Integer, Map<String, ObjectCache.vertexVectors>> vertices) {
		for (MeshRecord.Object.Face face : object.faces()) {
			ResourceLocation textureLocation = model.getTextureLocation(face.texture_name(), base);
			VertexConsumer consumer = getVertexConsumer(buffer, textureLocation, base);

			renderFace(object, face, base, poseStack, consumer, packedLight, packedOverlay, color, vertices);
		}
	}

	default void renderObject(MeshRecord.Object object, T base, M model, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
							  Color color, Map<Integer, Map<String, ObjectCache.vertexVectors>> vertices) {
		for (MeshRecord.Object.Face face : object.faces()) {
			renderFace(object, face, base, poseStack, vertexConsumer, packedLight, packedOverlay, color, vertices);
		}
	}

	default void renderFace(MeshRecord.Object object, MeshRecord.Object.Face face, T base, PoseStack poseStack, VertexConsumer consumer,
							int packedLight, int packedOverlay, Color color, Map<Integer, Map<String, ObjectCache.vertexVectors>> vertices) {
		Matrix4f poseMatrix = poseStack.last().pose();
		Matrix3f normalMatrix = poseStack.last().normal();

		List<CompiledVertex> compiledVertices = new ArrayList<>();

		for (MeshRecord.Object.Vertex vertex : object.vertices()) {
			base.getCache().vertices.put(vertex.index(), new HashMap<>());

			float maxWeight = vertex.max_weight();
			Vector3f vertexPos = new Vector3f(vertex.position()).add(object.position());

			if (base.getCache().hasArmatureRecord() && vertex.weights().length > 0) {
				for (MeshRecord.Object.Vertex.Weight weight : vertex.weights()) {
					if (base.getCache().armature.isUpdated(weight.name())) {
						Optional<Bone> bone = base.getCache().armature.getBone(weight.name());
						float weightPercent = weight.weight() / maxWeight;

						if (bone.isPresent()) {
							Bone boneInst = bone.get();

							Matrix4f boneTransform = boneInst.getWorldTransform();

							Vector4f transformedPosition = new Vector4f(vertexPos, 1.0f);
							boneTransform.transform(transformedPosition);

							Vector3f position = new Vector3f(transformedPosition.x, transformedPosition.y, transformedPosition.z).mul(weightPercent);

							Matrix3f rotationMatrix = new Matrix3f(boneTransform);
							Vector3f transformedNormal = new Vector3f(face.normal());
							rotationMatrix.transform(transformedNormal);

							Vector3f normal = transformedNormal.mul(weightPercent);

							compiledVertices.add(new CompiledVertex(position, normal));

							if (!vertices.containsKey(vertex.index()))
								base.getCache().vertices.put(vertex.index(), new HashMap<>());
							base.getCache().vertices.get(vertex.index()).put(weight.name(), new ObjectCache.vertexVectors(position, normal));
						}
					} else if (vertices.containsKey(vertex.index())) {
						ObjectCache.vertexVectors vertexVectors = vertices.get(vertex.index()).getOrDefault(weight.name(),
								new ObjectCache.vertexVectors(new Vector3f(), new Vector3f()));

						compiledVertices.add(new CompiledVertex(vertexVectors.position(), vertexVectors.normal()));

						if (!vertices.containsKey(vertex.index()))
							base.getCache().vertices.put(vertex.index(), new HashMap<>());
						base.getCache().vertices.get(vertex.index()).put(weight.name(), vertexVectors);
					}
				}
			} else {
				compiledVertices.add(new CompiledVertex(vertexPos, face.normal()));
			}
		}

		for (int vertexIndex : face.vertices()) {
			float[] uv = face.vertex_uvs().get(vertexIndex);
			CompiledVertex compiledVertex = compiledVertices.get(vertexIndex);
			vertex(poseMatrix, normalMatrix, consumer, color,
					compiledVertex.position(), new Vector2f(uv[0], 1 - uv[1]), compiledVertex.normal(),
					packedLight, packedOverlay);
		}
	}

	static void vertex(Matrix4f pose, Matrix3f normal, VertexConsumer vertexConsumer, Color color,
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

		float direction = getYRotation(blockState);
		stack.mulPose(Axis.YP.rotationDegrees(direction));

		if (blockState.hasProperty(BlockStateProperties.ATTACH_FACE)) {
			AttachFace face = blockState.getValue(BlockStateProperties.ATTACH_FACE);
			switch (face) {
				case CEILING -> stack.mulPose(Axis.XP.rotationDegrees(180));
				case WALL -> stack.mulPose(Axis.XP.rotationDegrees(90));
			}
		}

		stack.translate(0, -0.5f, 0);
	}

	default float getYRotation(BlockState blockState) {
		if (blockState.hasProperty(BlockStateProperties.ROTATION_16))
			return (360f/16f) * blockState.getValue(BlockStateProperties.ROTATION_16);

		if (blockState.hasProperty(HorizontalDirectionalBlock.FACING))
			return -blockState.getValue(HorizontalDirectionalBlock.FACING).toYRot();

		if (blockState.hasProperty(DirectionalBlock.FACING))
			return -blockState.getValue(DirectionalBlock.FACING).toYRot();

		return 0;
	}
}

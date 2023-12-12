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
import java.util.*;

@Environment(EnvType.CLIENT)
public interface MeshRenderer<T extends MeshAnimatable, M extends me.pandamods.pandalib.client.model.MeshModel<T>> {

	default RenderType getRenderType(ResourceLocation location, T base) {
		return RenderType.entityCutout(location);
	}

	default VertexConsumer getVertexConsumer(MultiBufferSource bufferSource, ResourceLocation location, T base) {
		return bufferSource.getBuffer(getRenderType(location, base));
	}

	default void renderRig(T base, M model, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		fullRenderRig(base, model, poseStack, buffer, packedLight, packedOverlay);
	}

	default void renderRig(T base, M model, PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
		fullRenderRig(base, model, poseStack, vertexConsumer, packedLight, packedOverlay);
	}

	private void fullRenderRig(T base, M model, PoseStack poseStack, Object buffer, int packedLight, int packedOverlay) {
		if (base.getCache() == null) {
			return;
		}

		poseStack.pushPose();
		base.getCache().updateMeshCache(model, base);

		Armature armature = base.getCache().armature;
		if (armature != null) {
			animateArmature(base, model, armature);
		}

		MeshRecord meshRecord = base.getCache().meshRecord;
		if (meshRecord != null) {
			if (buffer instanceof MultiBufferSource) {
				renderMesh(base, model, armature, meshRecord, poseStack, (MultiBufferSource) buffer, packedLight, packedOverlay);
			} else if (buffer instanceof VertexConsumer) {
				renderMesh(base, model, armature, meshRecord, poseStack, (VertexConsumer) buffer, packedLight, packedOverlay);
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
		Map<String, Map<Integer, ObjectCache.VertexCache>> cacheVertices = new HashMap<>(base.getCache().vertices);
		Map<String, Map<Integer, ObjectCache.FaceCache>> cacheFaces = new HashMap<>(base.getCache().faces);

		base.getCache().vertices.clear();
		base.getCache().faces.clear();

		for (Map.Entry<String, MeshRecord.Object> meshEntry : mesh.objects().entrySet()) {
			if (armature == null || !armature.getVisibility(meshEntry.getKey())) {
				base.getCache().vertices.put(meshEntry.getKey(), new HashMap<>());
				base.getCache().faces.put(meshEntry.getKey(), new HashMap<>());

				if (buffer instanceof MultiBufferSource) {
					renderObject(meshEntry.getValue(), base, model, poseStack, (MultiBufferSource) buffer, packedLight, packedOverlay, Color.WHITE,
							cacheVertices.getOrDefault(meshEntry.getKey(), new HashMap<>()),
							cacheFaces.getOrDefault(meshEntry.getKey(), new HashMap<>()),
							base.getCache().vertices.get(meshEntry.getKey()),
							base.getCache().faces.get(meshEntry.getKey())
					);
				} else if (buffer instanceof VertexConsumer) {
					renderObject(meshEntry.getValue(), base, model, poseStack, (VertexConsumer) buffer, packedLight, packedOverlay, Color.WHITE,
							cacheVertices.getOrDefault(meshEntry.getKey(), new HashMap<>()),
							cacheFaces.getOrDefault(meshEntry.getKey(), new HashMap<>()),
							base.getCache().vertices.get(meshEntry.getKey()),
							base.getCache().faces.get(meshEntry.getKey())
					);
				}
			}
		}

		if (armature != null) {
			armature.clearUpdatedBones();
		}
	}

	default void renderObject(MeshRecord.Object object, T base, M model, PoseStack poseStack, MultiBufferSource buffer,
							  int packedLight, int packedOverlay, Color color,
							  Map<Integer, ObjectCache.VertexCache> cachedVertices, Map<Integer, ObjectCache.FaceCache> cachedFaces,
							  Map<Integer, ObjectCache.VertexCache> newCachedVertices, Map<Integer, ObjectCache.FaceCache> newCachedFaces) {
		Map<Integer, CompiledVertex> compiledVertices = compileVertices(object, base, model, poseStack.last(), cachedVertices, newCachedVertices);

		for (int i = 0; i < object.faces().length; i++) {
			MeshRecord.Object.Face face = object.faces()[i];

			Map<String, ResourceLocation> textureLocations = model.getTextureLocations(base);
			ResourceLocation textureLocation = textureLocations.getOrDefault(face.texture_name(),
					textureLocations.getOrDefault("",
							!textureLocations.isEmpty() ? textureLocations.values().iterator().next() : null));
			VertexConsumer vertexConsumer = getVertexConsumer(buffer, textureLocation, base);

			renderFace(face, i, base, poseStack.last(), vertexConsumer, packedLight, packedOverlay, color,
					compiledVertices, cachedFaces, newCachedFaces);
		}
	}

	default void renderObject(MeshRecord.Object object, T base, M model, PoseStack poseStack, VertexConsumer vertexConsumer,
							  int packedLight, int packedOverlay, Color color,
							  Map<Integer, ObjectCache.VertexCache> cachedVertices, Map<Integer, ObjectCache.FaceCache> cachedFaces,
							  Map<Integer, ObjectCache.VertexCache> newCachedVertices, Map<Integer, ObjectCache.FaceCache> newCachedFaces) {
		Map<Integer, CompiledVertex> compiledVertices = compileVertices(object, base, model, poseStack.last(), cachedVertices, newCachedVertices);

		for (int i = 0; i < object.faces().length; i++) {
			MeshRecord.Object.Face face = object.faces()[i];
			renderFace(face, i, base, poseStack.last(), vertexConsumer, packedLight, packedOverlay, color,
					compiledVertices, cachedFaces, newCachedFaces);
		}
	}

	default Map<Integer, CompiledVertex> compileVertices(MeshRecord.Object object, T base, M model, PoseStack.Pose pose,
														 Map<Integer, ObjectCache.VertexCache> cachedVertices,
														 Map<Integer, ObjectCache.VertexCache> newCachedVertices) {
		Map<Integer, CompiledVertex> compiledVertices = new HashMap<>();

		for (MeshRecord.Object.Vertex vertex : object.vertices()) {
			float maxWeight = vertex.max_weight();
			Vector3f vertexPos = new Vector3f(vertex.position()).add(object.position());

			if (base.getCache().hasArmature() && vertex.weights().length > 0) {
				Armature armature = base.getCache().armature;
				if (Arrays.stream(vertex.weights()).anyMatch(weight -> armature.isUpdated(weight.name())) ||
						!cachedVertices.containsKey(vertex.index())) {
					for (MeshRecord.Object.Vertex.Weight weight : vertex.weights()) {
						Optional<Bone> boneOptional = armature.getBone(weight.name());
						float weightPercent = weight.weight() / maxWeight;

						if (boneOptional.isPresent()) {
							Bone bone = boneOptional.get();
							Matrix4f boneWorldTransform = bone.getWorldTransform();

							Vector3f originalVertexPos = new Vector3f(vertexPos);

							Vector4f vertexPositionHomogeneous = new Vector4f(vertexPos, 1.0f);
							boneWorldTransform.transform(vertexPositionHomogeneous);
							vertexPos.set(vertexPositionHomogeneous.x, vertexPositionHomogeneous.y, vertexPositionHomogeneous.z);

							Vector3f positionDifference = new Vector3f(vertexPos).sub(originalVertexPos);

							positionDifference.mul(weightPercent);

							vertexPos.set(originalVertexPos.add(positionDifference));
						}
					}
				} else {
					vertexPos = cachedVertices.get(vertex.index()).position();
				}
			}

			compiledVertices.put(vertex.index(), new CompiledVertex(vertexPos, vertex));
			newCachedVertices.put(vertex.index(), new ObjectCache.VertexCache(vertexPos));
		}

		return compiledVertices;
	}

	default void renderFace(MeshRecord.Object.Face face, int faceIndex, T base, PoseStack.Pose pose, VertexConsumer consumer,
							int packedLight, int packedOverlay, Color color, Map<Integer, CompiledVertex> compiledVertices,
							Map<Integer, ObjectCache.FaceCache> cachedFaces, Map<Integer, ObjectCache.FaceCache> newCachedFaces) {
		for (int vertexIndex : face.vertices()) {
			float[] uv = face.vertex_uvs().get(vertexIndex);
			CompiledVertex compiledVertex = compiledVertices.get(vertexIndex);
			MeshRecord.Object.Vertex vertex = compiledVertex.data();

			float maxWeight = vertex.max_weight();
			Vector3f normal = new Vector3f(face.normal());

			if (base.getCache().hasArmature() && vertex.weights().length > 0) {
				Armature armature = base.getCache().armature;
				if (Arrays.stream(vertex.weights()).anyMatch(weight -> armature.isUpdated(weight.name())) ||
						!cachedFaces.containsKey(faceIndex)) {
					for (MeshRecord.Object.Vertex.Weight weight : vertex.weights()) {
						Optional<Bone> boneOptional = armature.getBone(weight.name());
						float weightPercent = weight.weight() / maxWeight;

						if (boneOptional.isPresent()) {
							Bone bone = boneOptional.get();
							Matrix4f boneWorldTransform = bone.getWorldTransform();

							Vector3f originalNormal = new Vector3f(normal);

							Matrix3f rotationMatrix = new Matrix3f(boneWorldTransform);
							Vector3f normalHomogeneous = new Vector3f(normal);
							rotationMatrix.transform(normalHomogeneous);
							normal.set(normalHomogeneous.x, normalHomogeneous.y, normalHomogeneous.z).normalize();

							Vector3f normalDifference = new Vector3f(normal).sub(originalNormal);

							normalDifference.mul(weightPercent);

							normal.set(originalNormal.add(normalDifference)).normalize();
						}
					}
				} else {
					normal = cachedFaces.get(faceIndex).normal();
				}
			}

			newCachedFaces.put(faceIndex, new ObjectCache.FaceCache(normal));

			vertex(pose, consumer, color, compiledVertex.position(), new Vector2f(uv[0], 1 - uv[1]), normal, packedLight, packedOverlay);
		}
	}

	static void vertex(PoseStack.Pose pose, VertexConsumer vertexConsumer, Color color,
					   Vector3f pos, Vector2f uv, Vector3f normalVec, int packedLight, int packedOverlay) {
		vertexConsumer
				.vertex(pose.pose(), pos.x, pos.y, pos.z)
				.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())
				.uv(uv.x, uv.y)
				.overlayCoords(packedOverlay)
				.uv2(packedLight)
				.normal(pose.normal(), normalVec.x, normalVec.y, normalVec.z)
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

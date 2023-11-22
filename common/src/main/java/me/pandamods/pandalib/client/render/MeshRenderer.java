package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.cache.MeshCache;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.Bone;
import me.pandamods.pandalib.client.model.CompiledVertices;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.entity.MeshAnimatable;
import me.pandamods.pandalib.resources.Mesh;
import me.pandamods.pandalib.resources.Resources;
import me.pandamods.pandalib.utils.RenderUtils;
import me.pandamods.pandalib.utils.VectorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
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
import java.lang.Math;
import java.util.*;
import java.util.List;

@Environment(EnvType.CLIENT)
public interface MeshRenderer<T extends MeshAnimatable, M extends MeshModel<T>> {

	default RenderType getRenderType(ResourceLocation location, T base) {
		return RenderType.entityCutout(location);
	}

	default VertexConsumer getVertexConsumer(MultiBufferSource bufferSource, ResourceLocation location, T base) {
		return bufferSource.getBuffer(getRenderType(location, base));
	}

	default void renderRig(T base, M model, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay, boolean shouldRenderMesh) {
		if (base.getCache() != null) {
			stack.pushPose();

			ResourceLocation location = model.getMeshLocation(base);
			if (base.getCache().mesh == null || !base.getCache().meshLocation.equals(location)) {
				base.getCache().meshLocation = location;
				base.getCache().mesh = Resources.MESHES.getOrDefault(location, null);
				if (base.getCache().mesh != null) {
					base.getCache().armature = new Armature(base.getCache().mesh);
					model.setPropertiesOnCreation(base, base.getCache().armature);
				} else {
					PandaLib.LOGGER.error("Cant find mesh at " + model.getMeshLocation(base).toString());
				}
			}
			Mesh mesh = base.getCache().mesh;
			Armature armature = base.getCache().armature;
			if (armature != null)
				animateArmature(base, model, armature);
			if (shouldRenderMesh && mesh != null)
				renderMesh(base, model, armature, mesh, stack, buffer, packedLight, packedOverlay);
			stack.popPose();
		}
	}

	default void animateArmature(T base, M model, Armature armature) {
		float deltaSeconds = RenderUtils.getDeltaSeconds();

		if (base.getCache().animationController == null && model.createAnimationController() != null) {
			base.getCache().animationController = model.createAnimationController().create(base);
		}

		if (base.getCache().animationController != null) {
			base.getCache().animationController.updateAnimations(deltaSeconds);
		}

		model.setupAnim(base, armature, deltaSeconds);
	}

	default void renderMesh(T base, M model, @Nullable Armature armature, Mesh mesh, PoseStack stack,
							MultiBufferSource buffer, int packedLight, int packedOverlay) {
		Map<Integer, Map<String, MeshCache.vertexVectors>> vertices = new HashMap<>(base.getCache().vertices);
		base.getCache().vertices.clear();

		for (Map.Entry<String, Mesh.Object> meshEntry : mesh.objects().entrySet()) {
			if (armature == null || !armature.getVisibility(meshEntry.getKey()))
				renderObject(meshEntry.getValue(), base, model, stack, buffer, packedLight, packedOverlay, Color.WHITE, vertices);
		}

		if (armature != null) {
			armature.clearUpdatedBones();
		}
	}

	default void renderObject(Mesh.Object object, T base, M model, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay,
							  Color color, Map<Integer, Map<String, MeshCache.vertexVectors>> vertices) {
		for (Mesh.Object.Face face : object.faces()) {
			ResourceLocation textureLocation = model.getTextureLocation(face.texture_name(), base);
			VertexConsumer consumer = getVertexConsumer(buffer, textureLocation, base);

			renderFace(object, face, base, stack, consumer, packedLight, packedOverlay, color, vertices);
		}
	}

	default void renderObject(Mesh.Object object, T base, M model, PoseStack stack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay,
							  Color color, Map<Integer, Map<String, MeshCache.vertexVectors>> vertices) {
		for (Mesh.Object.Face face : object.faces()) {
			renderFace(object, face, base, stack, vertexConsumer, packedLight, packedOverlay, color, vertices);
		}
	}

	default void renderFace(Mesh.Object object, Mesh.Object.Face face, T base, PoseStack stack, VertexConsumer consumer,
							int packedLight, int packedOverlay, Color color, Map<Integer, Map<String, MeshCache.vertexVectors>> vertices) {
		Matrix4f poseMatrix = stack.last().pose();
		Matrix3f normalMatrix = stack.last().normal();

		List<CompiledVertices> compiledVertices = new ArrayList<>();

		for (Map.Entry<Integer, Mesh.Object.Face.Vertex> vertexEntry : face.vertices().entrySet()) {
			base.getCache().vertices.put(vertexEntry.getKey(), new HashMap<>());
			Mesh.Object.Face.Vertex vertex = vertexEntry.getValue();

			float maxWeight = (float) Arrays.stream(vertex.weights()).mapToDouble(Mesh.Object.Face.Vertex.Weight::weight).sum();
			Vector3f vertexPos = new Vector3f(vertex.position()).add(object.position());

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

							Matrix3f rotationMatrix = new Matrix3f(boneTransform);
							Vector3f transformedNormal = new Vector3f(face.normal());
							rotationMatrix.transform(transformedNormal);

							Vector3f normal = transformedNormal.mul(weightPercent);

							compiledVertices.add(new CompiledVertices(position, normal, new Vector2f(vertex.uv()[0],  1 - vertex.uv()[1])));

							if (!vertices.containsKey(vertexEntry.getKey()))
								base.getCache().vertices.put(vertexEntry.getKey(), new HashMap<>());
							base.getCache().vertices.get(vertexEntry.getKey()).put(weight.name(), new MeshCache.vertexVectors(position, normal));
						}
					} else if (vertices.containsKey(vertexEntry.getKey())) {
						MeshCache.vertexVectors vertexVectors = vertices.get(vertexEntry.getKey()).getOrDefault(weight.name(),
								new MeshCache.vertexVectors(new Vector3f(), new Vector3f()));

						compiledVertices.add(new CompiledVertices(vertexVectors.position(), vertexVectors.normal(),
								new Vector2f(vertex.uv()[0],  1 - vertex.uv()[1])));

						if (!vertices.containsKey(vertexEntry.getKey()))
							base.getCache().vertices.put(vertexEntry.getKey(), new HashMap<>());
						base.getCache().vertices.get(vertexEntry.getKey()).put(weight.name(), vertexVectors);
					}
				}
			} else {
				compiledVertices.add(new CompiledVertices(vertexPos, face.normal(),
						new Vector2f(vertex.uv()[0],  1 - vertex.uv()[1])));
			}
		}

		for (CompiledVertices compiledVertex : compiledVertices) {
			vertex(poseMatrix, normalMatrix, consumer, color,
					compiledVertex.position(), compiledVertex.uv(), compiledVertex.normal(),
					packedLight, packedOverlay);
		}
	}

	static boolean shouldRenderFace(PoseStack poseStack) {
		Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;
		Vector3f cameraPosition = camera.getPosition().toVector3f();
		Quaternionf cameraRotation = new Quaternionf().identity().setFromUnnormalized(new Matrix4f().translate(camera.getLookVector()));
		cameraRotation.normalize();

		Vector3f objectPosition = poseStack.last().pose().getTranslation(new Vector3f());

		Vector3f relativeCamPosition = objectPosition.rotate(cameraRotation);

		System.out.println(VectorUtils.betterPrint(relativeCamPosition));
		return true;
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

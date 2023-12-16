package me.pandamods.extra_details.api.client.render.block.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.api.client.render.block.ClientBlock;
import me.pandamods.extra_details.api.client.render.block.ClientBlockRenderer;
import me.pandamods.pandalib.cache.ObjectCache;
import me.pandamods.pandalib.client.render.MeshRenderer;
import me.pandamods.pandalib.entity.MeshAnimatable;
import me.pandamods.pandalib.resources.MeshRecord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Map;
import java.util.SortedSet;

public abstract class MeshClientBlockRenderer<T extends ClientBlock & MeshAnimatable, M extends me.pandamods.pandalib.client.model.MeshModel<T>>
		implements ClientBlockRenderer<T>, MeshRenderer<T, M> {
	protected final M model;

	public MeshClientBlockRenderer(M model) {
		this.model = model;
	}

	@Override
	public void render(T block, PoseStack poseStack, MultiBufferSource buffer, int lightColor, int overlay, float partialTick) {
		poseStack.pushPose();
		translateBlock(block.getBlockState(), poseStack);
		this.renderRig(block, this.model, poseStack, buffer, lightColor, overlay);
		poseStack.popPose();
	}

	@Override
	public void renderObject(MeshRecord.Object object, T base, M model, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay,
							 Color color,
							 Map<Integer, ObjectCache.VertexCache> cachedVertices, Map<Integer, ObjectCache.FaceCache> cachedFaces,
							 Map<Integer, ObjectCache.VertexCache> newCachedVertices, Map<Integer, ObjectCache.FaceCache> newCachedFaces) {
		MeshRenderer.super.renderObject(object, base, model, stack, buffer, packedLight, packedOverlay, color,
				cachedVertices, cachedFaces, newCachedVertices, newCachedFaces);

		BlockPos blockPos = base.getBlockPos();
		if (blockPos != null) {
			SortedSet<BlockDestructionProgress> sortedSet = Minecraft.getInstance()
					.levelRenderer.destructionProgress.get(blockPos.asLong());
			int progress;
			if (sortedSet != null && !sortedSet.isEmpty() && (progress = sortedSet.last().getProgress()) >= 0) {
				VertexConsumer destroyConsumer = new SheetedDecalTextureGenerator(Minecraft.getInstance().renderBuffers().crumblingBufferSource()
						.getBuffer(ModelBakery.DESTROY_TYPES.get(progress)),
						stack.last().pose().translate(0.5f, 0f, 0.5f, new Matrix4f()), stack.last().normal(), 1.0f);
				this.renderObject(object, base, model, stack, destroyConsumer, packedLight, packedOverlay, color,
						cachedVertices, cachedFaces, newCachedVertices, newCachedFaces);
			}
		}
	}
}

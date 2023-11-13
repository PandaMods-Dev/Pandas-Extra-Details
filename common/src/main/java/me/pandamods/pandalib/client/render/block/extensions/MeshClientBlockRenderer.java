package me.pandamods.pandalib.client.render.block.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import me.pandamods.pandalib.cache.MeshCache;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.client.render.MeshRenderer;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.client.render.block.ClientBlockRenderer;
import me.pandamods.pandalib.entity.MeshAnimatable;
import me.pandamods.pandalib.resources.Mesh;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Map;
import java.util.SortedSet;

public abstract class MeshClientBlockRenderer<T extends ClientBlock & MeshAnimatable, M extends MeshModel<T>>
		implements ClientBlockRenderer<T>, MeshRenderer<T, M> {
	protected final M model;

	public MeshClientBlockRenderer(M model) {
		this.model = model;
	}

	@Override
	public void render(T block, PoseStack poseStack, MultiBufferSource buffer, int lightColor, int overlay, float partialTick) {
		poseStack.pushPose();
		translateBlock(block.getBlockState(), poseStack);
		this.renderRig(block, this.model, poseStack, buffer, lightColor, overlay, true);
		poseStack.popPose();
	}

	@Override
	public void renderObject(Mesh.Object object, T base, M model, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay,
							 Color color, Map<Integer, Map<String, MeshCache.vertexVectors>> vertices) {
		MeshRenderer.super.renderObject(object, base, model, stack, buffer, packedLight, packedOverlay, color, vertices);

		BlockPos blockPos = base.getBlockPos();
		if (blockPos != null) {
			SortedSet<BlockDestructionProgress> sortedSet = Minecraft.getInstance()
					.levelRenderer.destructionProgress.get(blockPos.asLong());
			int progress;
			if (sortedSet != null && !sortedSet.isEmpty() && (progress = sortedSet.last().getProgress()) >= 0) {
				VertexConsumer destroyConsumer = new SheetedDecalTextureGenerator(Minecraft.getInstance().renderBuffers().crumblingBufferSource()
						.getBuffer(ModelBakery.DESTROY_TYPES.get(progress)),
						stack.last().pose().translate(0.5f, 0f, 0.5f, new Matrix4f()), stack.last().normal(), 1.0f);
				this.renderObject(object, base, model, stack, destroyConsumer, packedLight, packedOverlay, color, vertices);
			}
		}
	}
}

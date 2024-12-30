package me.pandamods.extra_details.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
import com.mojang.blaze3d.resource.ResourceHandle;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.pandamods.extra_details.api.render.ExtraDetailsBlockRenderDispatcher;
import me.pandamods.extra_details.client.ExtraDetailsClient;
import me.pandamods.extra_details.extensions.CompiledSectionExtension;
import me.pandamods.extra_details.extensions.LevelRendererExtension;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.SortedSet;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin implements LevelRendererExtension {
	@Shadow @Final private ObjectArrayList<SectionRenderDispatcher.RenderSection> visibleSections;

	@Shadow @Final private Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress;

	@Shadow @Final private Minecraft minecraft;

	@Shadow private @Nullable ClientLevel level;

	@Inject(method = "renderLevel", at = @At("HEAD"))
	public void renderLevel(GraphicsResourceAllocator graphicsResourceAllocator, DeltaTracker deltaTracker, boolean bl, Camera camera, 
							GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, Matrix4f matrix4f2, CallbackInfo ci) {
		ExtraDetailsBlockRenderDispatcher blockRenderDispatcher = ExtraDetailsClient.getInstance().blockRenderDispatcher;
		blockRenderDispatcher.prepare(level, camera, minecraft.hitResult);
	}

	@Override
	public void extraDetails$renderBlocks(PoseStack poseStack,
										  MultiBufferSource.BufferSource bufferSource,
										  MultiBufferSource.BufferSource bufferSource2,
										  Camera camera, float partialTick) {
		ExtraDetailsBlockRenderDispatcher blockRenderDispatcher = ExtraDetailsClient.getInstance().blockRenderDispatcher;
		
		Vec3 camVec = camera.getPosition();
		double camX = camVec.x();
		double camY = camVec.y();
		double camZ = camVec.z();

		for (SectionRenderDispatcher.RenderSection renderSection : this.visibleSections) {
			List<BlockPos> list = ((CompiledSectionExtension) renderSection.getCompiled()).extraDetails$getRenderableBlocks();
			if (list.isEmpty()) continue;
			for (BlockPos blockPos : list) {
				MultiBufferSource multiBufferSource = bufferSource;
				poseStack.pushPose();
				poseStack.translate((double) blockPos.getX() - camX, (double) blockPos.getY() - camY, (double) blockPos.getZ() - camZ);
				SortedSet<BlockDestructionProgress> sortedSet = this.destructionProgress.get(blockPos.asLong());
				if (sortedSet != null && !sortedSet.isEmpty()) {
					int i = sortedSet.last().getProgress();
					if (i >= 0) {
						PoseStack.Pose pose = poseStack.last();
						VertexConsumer vertexConsumer = new SheetedDecalTextureGenerator(bufferSource2.getBuffer((RenderType) ModelBakery.DESTROY_TYPES.get(i)), pose, 1.0F);
						multiBufferSource = renderType -> {
							VertexConsumer vertexConsumer2 = bufferSource.getBuffer(renderType);
							return renderType.affectsCrumbling() ? VertexMultiConsumer.create(vertexConsumer, vertexConsumer2) : vertexConsumer2;
						};
					}
				}
				blockRenderDispatcher.render(blockPos, partialTick, poseStack, multiBufferSource);
				poseStack.popPose();
			}
		}
	}
}

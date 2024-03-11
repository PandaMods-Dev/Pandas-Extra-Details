package me.pandamods.extra_details.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.SheetedDecalTextureGenerator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.clientblockentity.renderer.ClientBlockEntityRenderDispatcher;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.BlockDestructionProgress;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.SortedSet;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
	@Shadow @Final private RenderBuffers renderBuffers;

	@Shadow @Nullable private ClientLevel level;

	@Shadow @Final private ObjectArrayList<LevelRenderer.RenderChunkInfo> renderChunksInFrustum;

	@Shadow @Final private Long2ObjectMap<SortedSet<BlockDestructionProgress>> destructionProgress;

	@Shadow @Final private Minecraft minecraft;
	private final ClientBlockEntityRenderDispatcher clientBlocEntityRenderDispatcher = ExtraDetails.blockRenderDispatcher;

	@Inject(
			method = "renderLevel",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;prepare(Lnet/minecraft/world/level/Level;Lnet/minecraft/client/Camera;Lnet/minecraft/world/phys/HitResult;)V",
					ordinal = 0
			)
	)
	public void prepareRenderLevel(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera,
								   GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
		this.clientBlocEntityRenderDispatcher.prepare(this.level, camera, this.minecraft.hitResult);
	}

	@Inject(
			method = "renderLevel",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/client/renderer/LevelRenderer;globalBlockEntities:Ljava/util/Set;",
					shift = At.Shift.BEFORE,
					ordinal = 0
			)
	)
	public void renderLevel(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera,
							GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
		Vec3 cameraPosition = camera.getPosition();
		double camX = cameraPosition.x;
		double camY = cameraPosition.y;
		double camZ = cameraPosition.z;

		if (this.level != null && !this.renderChunksInFrustum.isEmpty()) {
			for (LevelRenderer.RenderChunkInfo renderChunkInfo : this.renderChunksInFrustum) {
				List<ClientBlockEntity> blockEntities = renderChunkInfo.chunk.getCompiledChunk().getClientBlockEntities();
				if (blockEntities.isEmpty()) continue;

				for (ClientBlockEntity blockEntity : blockEntities) {
					BlockPos blockPos = blockEntity.getBlockPos();
					poseStack.pushPose();
					poseStack.translate(blockPos.getX() - camX, blockPos.getY() - camY, blockPos.getZ() - camZ);
					MultiBufferSource bufferSource = this.renderBuffers.bufferSource();

					SortedSet<BlockDestructionProgress> breakingInfo = this.destructionProgress.get(blockPos.asLong());
					if (breakingInfo != null && !breakingInfo.isEmpty()) {
						int stage = breakingInfo.last().getProgress();

						if (stage >= 0) {
							VertexConsumer bufferBuilder = this.renderBuffers.crumblingBufferSource()
									.getBuffer(ModelBakery.DESTROY_TYPES.get(stage));

							PoseStack.Pose pose = poseStack.last();
							VertexConsumer crumblingConsumer = new SheetedDecalTextureGenerator(bufferBuilder, pose.pose(), pose.normal(), 1);

							bufferSource = (renderType) -> renderType.affectsCrumbling() ? VertexMultiConsumer
									.create(crumblingConsumer, this.renderBuffers.bufferSource().getBuffer(renderType)) :
									this.renderBuffers.bufferSource().getBuffer(renderType);
						}
					}

					clientBlocEntityRenderDispatcher.render(blockEntity, partialTick, poseStack, bufferSource);
					poseStack.popPose();
				}
			}
		}
	}
}

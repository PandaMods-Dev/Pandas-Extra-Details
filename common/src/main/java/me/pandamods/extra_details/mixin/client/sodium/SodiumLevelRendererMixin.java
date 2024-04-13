package me.pandamods.extra_details.mixin.client.sodium;

import com.mojang.blaze3d.vertex.PoseStack;
import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderList;
import me.jellysquid.mods.sodium.client.render.chunk.region.RenderRegion;
import me.jellysquid.mods.sodium.client.util.iterator.ByteIterator;
import me.jellysquid.mods.sodium.client.world.WorldRendererExtended;
import me.pandamods.extra_details.pandalib.impl.CompileResultsExtension;
import me.pandamods.extra_details.api.utils.ClientBlockUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.*;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;

@Mixin(LevelRenderer.class)
public abstract class SodiumLevelRendererMixin {
	@Shadow @Final private RenderBuffers renderBuffers;

	@Shadow @Nullable private ClientLevel level;

	@Inject(
			method = "renderLevel",
			at = @At(
					value = "INVOKE",
					target = "Lit/unimi/dsi/fastutil/longs/Long2ObjectMap;long2ObjectEntrySet()Lit/unimi/dsi/fastutil/objects/ObjectSet;",
					shift = At.Shift.BY, by = -2
			), locals = LocalCapture.CAPTURE_FAILHARD
	)
	public void renderLevel(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera,
							GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
		MultiBufferSource.BufferSource buffersource = this.renderBuffers.bufferSource();
		Vec3 cameraPosition = camera.getPosition();

		if (this.level != null) {
			SodiumWorldRenderer sodiumWorld = ((WorldRendererExtended) this).sodium$getWorldRenderer();
			RenderSectionManager renderSectionManager = ((SodiumWorldRendererAccessor) sodiumWorld).getRenderSectionManager();
			Iterator<ChunkRenderList> iterator = renderSectionManager.getRenderLists().iterator();
			while (iterator.hasNext()) {
				ChunkRenderList renderList = iterator.next();

				RenderRegion renderRegion = renderList.getRegion();
				ByteIterator renderSectionIterator = renderList.sectionsWithEntitiesIterator();

				if (renderSectionIterator == null) {
					continue;
				}

				while (renderSectionIterator.hasNext()) {
					int renderSectionId = renderSectionIterator.nextByteAsInt();
                	RenderSection renderSection = renderRegion.getSection(renderSectionId);

					ClientBlockUtils.render(poseStack, ((CompileResultsExtension) renderSection), cameraPosition,
							buffersource, partialTick);
				}
			}
		}
	}
}

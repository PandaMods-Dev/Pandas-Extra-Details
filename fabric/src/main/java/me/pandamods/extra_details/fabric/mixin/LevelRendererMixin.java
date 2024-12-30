package me.pandamods.extra_details.fabric.mixin;

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
public abstract class LevelRendererMixin {
	@Shadow protected abstract void checkPoseStack(PoseStack poseStack);

	@Inject(method = "method_62214", at = @At(
			 value = "INVOKE_STRING",
			 target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V",
			 args = "ldc=blockentities"
	))
	public void renderBlocks(FogParameters fogParameters, DeltaTracker deltaTracker,
							 Camera camera, ProfilerFiller profilerFiller, Matrix4f matrix4f, Matrix4f matrix4f2,
							 ResourceHandle resourceHandle, ResourceHandle resourceHandle2, ResourceHandle resourceHandle3, ResourceHandle resourceHandle4,
							 boolean bl, Frustum frustum, ResourceHandle resourceHandle5, CallbackInfo ci,
							 @Local(name = "poseStack") PoseStack poseStack,
							 @Local(name = "bufferSource") MultiBufferSource.BufferSource bufferSource,
							 @Local(name = "bufferSource2") MultiBufferSource.BufferSource bufferSource2,
							 @Local(name = "f") float partialTick) {
		profilerFiller.push("extra_details_blocks");
		((LevelRendererExtension) this).extraDetails$renderBlocks(poseStack, bufferSource, bufferSource2, camera, partialTick);
		bufferSource.endLastBatch();
		this.checkPoseStack(poseStack);
	}
}

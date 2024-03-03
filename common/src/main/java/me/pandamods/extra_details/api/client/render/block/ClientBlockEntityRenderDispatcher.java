package me.pandamods.extra_details.api.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntityType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.HitResult;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ClientBlockEntityRenderDispatcher implements ResourceManagerReloadListener {
	private Map<ClientBlockEntityType<?>, ClientBlockEntityRenderer<?>> renderers = new HashMap<>();
	private ClientLevel level;
	private Camera camera;
	private HitResult hitResult;

	@SuppressWarnings("unchecked")
	public <E extends ClientBlockEntity> ClientBlockEntityRenderer<E> getRenderer(E blockEntity) {
		return (ClientBlockEntityRenderer<E>) this.renderers.get(blockEntity.getType());
	}

	public <E extends ClientBlockEntity> void render(E blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource) {
		ClientBlockEntityRenderer<E> blockEntityRenderer = this.getRenderer(blockEntity);
		if (blockEntityRenderer != null) {
			if (blockEntity.hasLevel() && blockEntity.getType().isValid(blockEntity.getBlockState())) {
				if (blockEntityRenderer.shouldRender(blockEntity, this.camera.getPosition())) {
					tryRender(blockEntity, () -> setupAndRender(blockEntityRenderer, blockEntity, partialTick, poseStack, bufferSource));
				}
			}
		}
	}

	private static <T extends ClientBlockEntity> void setupAndRender(ClientBlockEntityRenderer<T> renderer, T blockEntity, float partialTick,
															   PoseStack poseStack, MultiBufferSource bufferSource) {
		renderer.render(blockEntity, poseStack, bufferSource, partialTick, getLightColor(blockEntity.getLevel(), blockEntity.getBlockPos()));
	}

	private static void tryRender(ClientBlockEntity blockEntity, Runnable renderer) {
		try {
			renderer.run();
		} catch (Throwable var5) {
			CrashReport crashReport = CrashReport.forThrowable(var5, "Rendering Block Entity");
//			CrashReportCategory crashReportCategory = crashReport.addCategory("Block Entity Details");
//			blockEntity.fillCrashReportCategory(crashReportCategory);
			throw new ReportedException(crashReport);
		}
	}

	public static int getLightColor(Level level, BlockPos blockPos) {
		if (level != null) {
			return LevelRenderer.getLightColor(level, blockPos);
		} else {
			return  15728880;
		}
	}

	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		ClientBlockEntityRendererRegistry rendererRegistry = new ClientBlockEntityRendererRegistry();
		ExtraDetails.rendererRegistryEvent.invoker().register(rendererRegistry);
		renderers = rendererRegistry.renderers;
	}

	public void prepare(ClientLevel level, Camera camera, HitResult hitResult) {
		this.level = level;
		this.camera = camera;
		this.hitResult = hitResult;
	}
}

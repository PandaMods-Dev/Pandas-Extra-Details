package me.pandamods.extra_details.api.clientblockentity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;

public interface ClientBlockEntityRenderer<T extends ClientBlockEntity> {
	void render(T blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick, int lightColor);

	default int getViewDistance() {
		return 64;
	}

	default boolean shouldRender(T blockEntity, Vec3 cameraPos) {
		return Vec3.atCenterOf(blockEntity.getBlockPos()).closerThan(cameraPos, this.getViewDistance());
	}
}

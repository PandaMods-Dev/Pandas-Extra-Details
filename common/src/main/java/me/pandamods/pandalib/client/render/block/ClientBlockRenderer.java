package me.pandamods.pandalib.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public interface ClientBlockRenderer<T extends ClientBlock> {
	void render(T block, PoseStack poseStack, MultiBufferSource buffer, int lightColor, int overlay, float partialTick);

	default RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	default int getViewDistance() {
		return 64;
	}

	default boolean shouldRender(T clientBlock, Vec3 cameraPos) {
		return Vec3.atCenterOf(clientBlock.getBlockPos()).closerThan(cameraPos, this.getViewDistance());
	}
}

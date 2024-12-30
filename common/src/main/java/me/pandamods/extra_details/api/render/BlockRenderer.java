/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.extra_details.api.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public interface BlockRenderer {
	void render(BlockPos blockPos, ClientLevel level, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay);

	default boolean shouldRenderOffScreen(BlockPos blockPos, ClientLevel level) {
		return false;
	}

	default int getViewDistance() {
		return 64;
	}

	default boolean shouldRender(BlockPos blockPos, ClientLevel level, Vec3 cameraPos) {
		return Vec3.atCenterOf(blockPos).closerThan(cameraPos, this.getViewDistance());
	}
}

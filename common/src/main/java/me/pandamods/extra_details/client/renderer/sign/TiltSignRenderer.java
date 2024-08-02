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

package me.pandamods.extra_details.client.renderer.sign;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.utils.MathUtils;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Math;
import org.joml.Random;
import org.joml.Vector3f;

// Todo Make some improvements to Standing Sign Renderer
public class TiltSignRenderer extends SignRenderer {
	public TiltSignRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public void render(SignBlockEntity signBlockEntity, float f, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j) {
		poseStack.pushPose();
		if (!(signBlockEntity.getBlockState().getBlock() instanceof WallSignBlock)) {
			float tiltX = ExtraDetails.CONFIG.get().blockSettings.sign.pitchTilt;
			float tiltY = ExtraDetails.CONFIG.get().blockSettings.sign.yawTilt;
			float tiltZ = ExtraDetails.CONFIG.get().blockSettings.sign.rollTilt;

			if (ExtraDetails.CONFIG.get().blockSettings.sign.randomTilt) {
				float minX = -ExtraDetails.CONFIG.get().blockSettings.sign.pitchTilt;
				float maxX = ExtraDetails.CONFIG.get().blockSettings.sign.pitchTilt;
				tiltX = new Random(
						signBlockEntity.getBlockPos().asLong()).nextFloat() * (maxX - minX) + minX;

				float minY = -ExtraDetails.CONFIG.get().blockSettings.sign.yawTilt;
				float maxY = ExtraDetails.CONFIG.get().blockSettings.sign.yawTilt;
				tiltY = new Random(
						(long) (signBlockEntity.getBlockPos().asLong() - signBlockEntity.getBlockPos().asLong() / 2.5)).nextFloat() * (maxY - minY) + minY;

				float minZ = -ExtraDetails.CONFIG.get().blockSettings.sign.rollTilt;
				float maxZ = ExtraDetails.CONFIG.get().blockSettings.sign.rollTilt;
				tiltZ = new Random(
						(long) (signBlockEntity.getBlockPos().asLong() + signBlockEntity.getBlockPos().asLong() / 1.5)).nextFloat() * (maxZ - minZ) + minZ;
			}

			Vector3f rotation = new Vector3f(tiltX, tiltY, tiltZ);
			BlockState state = signBlockEntity.getBlockState();
			rotation.rotateY(Math.toRadians(((SignBlock) state.getBlock()).getYRotationDegrees(state)));

			MathUtils.rotateByPivot(poseStack, new Vector3f(0.5f, 0, 0.5f), new Vector3f(
					Math.toRadians(rotation.x), Math.toRadians(rotation.y), Math.toRadians(rotation.z)));
		}
		super.render(signBlockEntity, f, poseStack, multiBufferSource, i, j);
		poseStack.popPose();
	}
}

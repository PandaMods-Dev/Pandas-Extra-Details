package me.pandamods.pandalib.utils;

import net.minecraft.core.BlockPos;
import org.joml.Math;

public class RandomUtils {
	public static float randomFloatFromBlockPos(BlockPos blockPos) {
		return (float) ((blockPos.getX() * blockPos.getY() * blockPos.getZ()) / Math.PI);
	}
}

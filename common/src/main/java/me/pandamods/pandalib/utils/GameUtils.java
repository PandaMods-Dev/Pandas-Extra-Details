package me.pandamods.pandalib.utils;

import net.minecraft.client.Minecraft;

public class GameUtils {
	public static float getDeltaSeconds() {
		return (float) 1 / Minecraft.getInstance().getFps();
	}
}

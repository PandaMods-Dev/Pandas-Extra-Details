package me.pandamods.extra_details.utils.forge;

import me.jellysquid.mods.sodium.client.world.WorldSlice;

public class ExtraDetailsSodiumPlatformImpl {
	public static int getLocalBlockIndex(int x, int y, int z) {
		return WorldSlice.getLocalBlockIndex(x, y, z);
	}

	public static int getLocalSectionIndex(int x, int y, int z) {
		return WorldSlice.getLocalSectionIndex(x, y, z);
	}
}

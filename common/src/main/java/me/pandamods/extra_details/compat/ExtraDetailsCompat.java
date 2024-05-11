package me.pandamods.extra_details.compat;

import dev.architectury.platform.Platform;

public class ExtraDetailsCompat {
	public static boolean isSodiumLoaded() {
		return Platform.isModLoaded("sodium") || Platform.isModLoaded("rubidium") || Platform.isModLoaded("embeddium");
	}
}

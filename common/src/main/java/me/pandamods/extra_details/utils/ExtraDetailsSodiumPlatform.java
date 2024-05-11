package me.pandamods.extra_details.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;

public class ExtraDetailsSodiumPlatform {
	@ExpectPlatform
	public static int getLocalBlockIndex(int x, int y, int z) {
		throw new AssertionError();
	}

	@ExpectPlatform
	public static int getLocalSectionIndex(int x, int y, int z) {
		throw new AssertionError();
	}
}

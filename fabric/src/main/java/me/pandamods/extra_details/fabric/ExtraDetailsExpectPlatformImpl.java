package me.pandamods.extra_details.fabric;

import me.pandamods.extra_details.ExtraDetailsExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class ExtraDetailsExpectPlatformImpl {
    /**
     * This is our actual method to {@link ExtraDetailsExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}

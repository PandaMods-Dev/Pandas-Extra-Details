package me.pandamods.extra_details.forge;

import me.pandamods.extra_details.ExtraDetailsExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class ExtraDetailsExpectPlatformImpl {
    /**
     * This is our actual method to {@link ExtraDetailsExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}

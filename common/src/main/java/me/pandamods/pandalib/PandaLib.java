package me.pandamods.pandalib;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import me.pandamods.pandalib.resources.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;

public class PandaLib {
	public static final String MOD_ID = "pandalib";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static void init() {
	}
}

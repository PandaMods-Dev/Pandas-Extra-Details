package me.pandamods.pandalib;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import me.pandamods.pandalib.resources.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.PackType;

public class PandaLib {
	public static final String MOD_ID = "pandalib";

	public static void init() {
		ClientLifecycleEvent.CLIENT_SETUP.register(PandaLib::clientInit);
	}

	private static void clientInit(Minecraft minecraft) {
		ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, Resources::reloadShaders);
	}
}

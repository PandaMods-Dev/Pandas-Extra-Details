package me.pandamods.pandalib;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import me.pandamods.pandalib.resources.Resources;
import net.minecraft.client.Minecraft;

public class PandaLib {
	public static final String MOD_ID = "pandalib";

	public static void init() {
		ClientLifecycleEvent.CLIENT_SETUP.register(PandaLib::clientInit);
	}

	private static void clientInit(Minecraft minecraft) {
		ClientReloadShadersEvent.EVENT.register(Resources::reloadShaders);
	}
}

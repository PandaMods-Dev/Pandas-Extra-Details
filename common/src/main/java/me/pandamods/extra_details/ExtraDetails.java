package me.pandamods.extra_details;

import com.mojang.logging.LogUtils;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import me.pandamods.extra_details.api.clientblockentity.renderer.ClientBlockEntityRenderDispatcher;
import me.pandamods.extra_details.api.clientblockentity.renderer.ClientBlockEntityRendererRegistry;
import me.pandamods.extra_details.registries.ClientBlockEntityRegistries;
import me.pandamods.pandalib.resource.Resources;
import org.slf4j.Logger;

public class ExtraDetails {
	public static final String MOD_ID = "extra_details";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final Resources RESOURCES = new Resources();
	public static final ClientBlockEntityRenderDispatcher blockRenderDispatcher = new ClientBlockEntityRenderDispatcher();
	public static final Event<RendererRegistryEvent> rendererRegistryEvent = EventFactory.createLoop();

	public static void init() {
	}

	public static void client() {
		ClientBlockEntityRegistries.init();
	}

	public interface RendererRegistryEvent {
		void register(ClientBlockEntityRendererRegistry registry);
	}
}
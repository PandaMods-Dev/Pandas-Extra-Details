package me.pandamods.extra_details;

import com.mojang.logging.LogUtils;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import me.pandamods.extra_details.api.client.render.block.ClientBlockEntityRenderDispatcher;
import me.pandamods.extra_details.api.client.render.block.ClientBlockEntityRendererRegistry;
import me.pandamods.extra_details.client.renderer.LeverRenderer;
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
		rendererRegistryEvent.register(registry -> {
			registry.register(ClientBlockEntityRegistries.LEVER, new LeverRenderer());
		});
	}

	public interface RendererRegistryEvent {
		void register(ClientBlockEntityRendererRegistry registry);
	}
}
package me.pandamods.extra_details;

import com.mojang.logging.LogUtils;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import me.pandamods.extra_details.api.clientblockentity.renderer.ClientBlockEntityRenderDispatcher;
import me.pandamods.extra_details.api.clientblockentity.renderer.ClientBlockEntityRendererRegistry;
import me.pandamods.extra_details.registries.ClientBlockEntityRegistries;
import me.pandamods.pandalib.client.render.PLInternalShaders;
import me.pandamods.pandalib.resource.Resources;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;

public class ExtraDetails {
	public static final String MOD_ID = "extra_details";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final Resources resources = new Resources();
	public static final ClientBlockEntityRenderDispatcher blockRenderDispatcher = new ClientBlockEntityRenderDispatcher();
	public static final ExtraDetailsLevelRenderer levelRenderer = new ExtraDetailsLevelRenderer(ExtraDetails.blockRenderDispatcher);
	public static final Event<RendererRegistryEvent> rendererRegistryEvent = EventFactory.createLoop();

	public static void init() {
	}

	public static void client() {
		ClientReloadShadersEvent.EVENT.register(PLInternalShaders::register);
		ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, ExtraDetails.resources);
		ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, ExtraDetails.blockRenderDispatcher);
		ClientBlockEntityRegistries.init();
	}

	public interface RendererRegistryEvent {
		void register(ClientBlockEntityRendererRegistry registry);
	}
}
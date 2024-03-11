package me.pandamods.extra_details.api.clientblockentity.renderer;

import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntityType;

import java.util.HashMap;
import java.util.Map;

public class ClientBlockEntityRendererRegistry {
	protected Map<ClientBlockEntityType<?>, ClientBlockEntityRenderer<?>> renderers = new HashMap<>();

	public void register(ClientBlockEntityType<?> blockEntityType, ClientBlockEntityRenderer<?> renderer) {
		renderers.put(blockEntityType, renderer);
	}
}
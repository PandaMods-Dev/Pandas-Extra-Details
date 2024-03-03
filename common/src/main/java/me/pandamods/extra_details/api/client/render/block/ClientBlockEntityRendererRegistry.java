package me.pandamods.extra_details.api.client.render.block;

import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntityType;

import java.util.HashMap;
import java.util.Map;

public class ClientBlockEntityRendererRegistry {
	protected Map<ClientBlockEntityType<?>, ClientBlockEntityRenderer<?>> renderers = new HashMap<>();

	public void register(ClientBlockEntityType<?> blockEntityType, ClientBlockEntityRenderer<?> renderer) {
		renderers.put(blockEntityType, renderer);
	}
}
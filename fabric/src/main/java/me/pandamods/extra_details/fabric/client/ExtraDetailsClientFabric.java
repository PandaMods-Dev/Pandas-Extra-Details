package me.pandamods.extra_details.fabric.client;

import me.pandamods.extra_details.client.ExtraDetailsClient;
import net.fabricmc.api.ClientModInitializer;

public class ExtraDetailsClientFabric implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		new ExtraDetailsClient();
	}
}

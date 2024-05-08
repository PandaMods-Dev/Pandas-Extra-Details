package me.pandamods.extra_details.fabric;

import me.pandamods.extra_details.ExtraDetails;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.CoreShaderRegistrationCallback;

public class ExtraDetailsFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ExtraDetails.client();
	}
}
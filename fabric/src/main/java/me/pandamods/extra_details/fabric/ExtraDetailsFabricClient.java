package me.pandamods.extra_details.fabric;

import me.pandamods.extra_details.ExtraDetails;
import net.fabricmc.api.ClientModInitializer;

public class ExtraDetailsFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ExtraDetails.client();
	}
}
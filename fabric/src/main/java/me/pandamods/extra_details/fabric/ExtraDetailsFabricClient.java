package me.pandamods.extra_details.fabric;

import me.pandamods.extra_details.ExtraDetails;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class ExtraDetailsFabricClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ExtraDetails.client();
	}
}
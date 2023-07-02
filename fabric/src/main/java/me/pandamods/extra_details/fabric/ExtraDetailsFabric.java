package me.pandamods.extra_details.fabric;

import me.pandamods.extra_details.ExtraDetails;
import net.fabricmc.api.ModInitializer;

public class ExtraDetailsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ExtraDetails.init();
    }
}
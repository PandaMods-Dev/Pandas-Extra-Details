package me.pandamods.extra_details.forge;

import dev.architectury.platform.forge.EventBuses;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.config.ExtraDetailsConfig;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExtraDetails.MOD_ID)
public class ExtraDetailsForge {
    public ExtraDetailsForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(ExtraDetails.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
		ExtraDetails.init();

		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
				new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) ->
						AutoConfig.getConfigScreen(ExtraDetailsConfig.class, screen).get()));
    }
}
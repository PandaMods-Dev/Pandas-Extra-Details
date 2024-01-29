package me.pandamods.extra_details.forge;

import dev.architectury.platform.forge.EventBuses;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.config.ModConfig;
import me.pandamods.pandalib.resources.Resources;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExtraDetails.MOD_ID)
public class ExtraDetailsForge {
    public ExtraDetailsForge() {
        EventBuses.registerModEventBus(ExtraDetails.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
//		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> Resources::registerReloadListener);
		ExtraDetails.init();
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ExtraDetails::client);

//		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
//				new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) ->
//						AutoConfig.getConfigScreen(ModConfig.class, screen).get()));
    }
}
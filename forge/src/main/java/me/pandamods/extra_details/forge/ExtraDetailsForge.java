package me.pandamods.extra_details.forge;

import dev.architectury.platform.forge.EventBuses;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.event.ClientResourceEvent;
import me.pandamods.pandalib.event.ResourceEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ExtraDetails.MOD_ID)
public class ExtraDetailsForge {
    public ExtraDetailsForge() {
        EventBuses.registerModEventBus(ExtraDetails.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
		ExtraDetails.init();

//		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
//				new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) ->
//						AutoConfig.getConfigScreen(ModConfig.class, screen).get()));
    }
}
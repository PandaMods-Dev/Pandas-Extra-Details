package me.pandamods.extra_details.forge;

import dev.architectury.platform.forge.EventBuses;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.resource.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
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
		MinecraftForge.EVENT_BUS.addListener(this::addReloadListenerEvent);
		ExtraDetails.init();
		DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ExtraDetails::client);

//		ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () ->
//				new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) ->
//						AutoConfig.getConfigScreen(ModConfig.class, screen).get()));
    }

	public void addReloadListenerEvent(AddReloadListenerEvent event) {
		event.addListener(ExtraDetails.BLOCK_RENDERER_DISPATCHER);
		event.addListener(ExtraDetails.RESOURCES);
	}
}
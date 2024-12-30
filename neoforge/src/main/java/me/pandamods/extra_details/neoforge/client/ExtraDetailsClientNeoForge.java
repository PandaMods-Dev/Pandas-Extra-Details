package me.pandamods.extra_details.neoforge.client;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.ExtraDetailsClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(value = ExtraDetails.MOD_ID, dist = Dist.CLIENT)
public class ExtraDetailsClientNeoForge {
	public ExtraDetailsClientNeoForge(IEventBus modBus) {
		new ExtraDetailsClient();
	}
}

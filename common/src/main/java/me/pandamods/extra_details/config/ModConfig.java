package me.pandamods.extra_details.config;

import me.pandamods.extra_details.ExtraDetails;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;

@Config(name = ExtraDetails.MOD_ID)
public class ModConfig extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("block_settings")
	@ConfigEntry.Gui.TransitiveObject
	public BlockSettingsConfig blockSettings = new BlockSettingsConfig();
}

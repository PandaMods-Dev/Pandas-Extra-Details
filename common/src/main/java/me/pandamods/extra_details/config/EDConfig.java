package me.pandamods.extra_details.config;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.config.blocks.SignSettings;
import me.pandamods.pandalib.api.config.Config;
import me.pandamods.pandalib.api.config.ConfigData;

@Config(name = "extra_details", modId = ExtraDetails.MOD_ID)
public class EDConfig implements ConfigData {
	public BlockSettings blockSettings = new BlockSettings();

	public static class BlockSettings {
		public SignSettings sign = new SignSettings();
	}
}

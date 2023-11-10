package me.pandamods.extra_details.config;

import me.pandamods.extra_details.config.block_settings.AnimatedBlockSettings;
import me.pandamods.extra_details.config.block_settings.SignSettings;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

import java.util.ArrayList;

@Config(name = "block_settings")
public class BlockSettingsConfig implements ConfigData {
	@ConfigEntry.Gui.CollapsibleObject
	public SignSettings sign = new SignSettings();

	@ConfigEntry.Gui.CollapsibleObject
	public AnimatedBlockSettings door = new AnimatedBlockSettings(new ArrayList<>());
	@ConfigEntry.Gui.CollapsibleObject
	public AnimatedBlockSettings trapdoor = new AnimatedBlockSettings(new ArrayList<>());
	@ConfigEntry.Gui.CollapsibleObject
	public AnimatedBlockSettings fenceGate = new AnimatedBlockSettings(new ArrayList<>());
	@ConfigEntry.Gui.CollapsibleObject
	public AnimatedBlockSettings lever = new AnimatedBlockSettings(new ArrayList<>());
	@ConfigEntry.Gui.CollapsibleObject
	public AnimatedBlockSettings chest = new AnimatedBlockSettings(new ArrayList<>());
}

package me.pandamods.extra_details.config;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.config.block_settings.AnimatedBlockSettings;
import me.pandamods.extra_details.config.block_settings.HangingSignSettings;
import me.pandamods.extra_details.config.block_settings.SignSettings;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Config(name = "block_settings")
public class BlockSettingsConfig implements ConfigData {
	@ConfigEntry.Gui.Tooltip
	public List<String> blacklist = new ArrayList<>();

	@ConfigEntry.Gui.CollapsibleObject
	public SignSettings sign = new SignSettings();
	@ConfigEntry.Gui.CollapsibleObject
	public HangingSignSettings hangingSign = new HangingSignSettings();

	@ConfigEntry.Gui.CollapsibleObject
	public AnimatedBlockSettings door = new AnimatedBlockSettings();
	@ConfigEntry.Gui.CollapsibleObject
	public AnimatedBlockSettings trapdoor = new AnimatedBlockSettings();
	@ConfigEntry.Gui.CollapsibleObject
	public AnimatedBlockSettings fenceGate = new AnimatedBlockSettings();
	@ConfigEntry.Gui.CollapsibleObject
	public AnimatedBlockSettings lever = new AnimatedBlockSettings();
	@ConfigEntry.Gui.CollapsibleObject
	public AnimatedBlockSettings chest = new AnimatedBlockSettings();
}

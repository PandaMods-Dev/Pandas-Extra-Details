package me.pandamods.extra_details.config;

import me.pandamods.extra_details.ExtraDetails;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

@Config(name = ExtraDetails.MOD_ID)
public class ModConfig extends PartitioningSerializer.GlobalData {
	@ConfigEntry.Category("block_settings")
	@ConfigEntry.Gui.TransitiveObject
	public BlockSettingsConfig blockSettings = new BlockSettingsConfig();

	public boolean isAllowed(Block block) {
		return !blockSettings.blacklist.contains(BuiltInRegistries.BLOCK.getKey(block).toString());
	}
}

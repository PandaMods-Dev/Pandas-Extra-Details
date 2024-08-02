/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

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

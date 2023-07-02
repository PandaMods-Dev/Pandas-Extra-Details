package me.pandamods.extra_details;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = ExtraDetails.MOD_ID)
public class ExtraDetailsConfig implements ConfigData {

	@ConfigEntry.Category("animation")
	public float door_animation_speed = 1;
	@ConfigEntry.Category("animation")
	public float trap_door_animation_speed = 1;

	@ConfigEntry.Category("detail_properties")
	@ConfigEntry.Gui.CollapsibleObject
	public SignDetails sign_details = new SignDetails();

	public static class SignDetails {
		public float pitch_tilt = 2;
		public float yaw_tilt = 5;
		public float roll_tilt = 5;
		public boolean random_tilt = true;
	}
}

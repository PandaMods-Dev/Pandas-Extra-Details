package me.pandamods.extra_details;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = ExtraDetails.MOD_ID)
public class ExtraDetailsConfig implements ConfigData {
	@ConfigEntry.Category("enabled_features")
	@ConfigEntry.Gui.Tooltip
	@ConfigEntry.Gui.RequiresRestart
	public boolean enable_door_animation = true;
	@ConfigEntry.Category("enabled_features")
	@ConfigEntry.Gui.Tooltip
	@ConfigEntry.Gui.RequiresRestart
	public boolean enable_trap_door_animation = true;
	@ConfigEntry.Category("enabled_features")
	public boolean enable_sign_tilt = true;

	@ConfigEntry.Category("animation")
	@ConfigEntry.Gui.Tooltip
	public float door_animation_length = 0.3f;
	@ConfigEntry.Category("animation")
	@ConfigEntry.Gui.Tooltip
	public float trap_door_animation_length = 0.3f;

	@ConfigEntry.Category("animation")
	public float fence_gate_animation_speed = 1;

	@ConfigEntry.Category("animation")
	public float lever_animation_speed = 1;

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

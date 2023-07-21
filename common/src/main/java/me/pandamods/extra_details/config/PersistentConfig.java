package me.pandamods.extra_details.config;

public class PersistentConfig {
	public static boolean enable_door_animation = true;
	public static boolean enable_trap_door_animation = true;
	public static boolean enable_fence_gate_animation = true;
	public static boolean enable_lever_animation = true;

	public static void init(ExtraDetailsConfig config) {
		enable_door_animation = config.enable_door_animation;
		enable_trap_door_animation = config.enable_trap_door_animation;
		enable_fence_gate_animation = config.enable_fence_gate_animation;
		enable_lever_animation = config.enable_lever_animation;
	}
}

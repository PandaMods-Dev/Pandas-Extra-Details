package me.pandamods.extra_details;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.common.ChunkEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import me.pandamods.extra_details.client.renderer.block.door.DoorRenderer;
import me.pandamods.extra_details.client.renderer.block.door.TrapDoorRenderer;
import me.pandamods.extra_details.client.renderer.block.sign.TiltSignRenderer;
import me.pandamods.extra_details.registries.BlockEntityRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.core.config.Loggers;
import org.slf4j.Logger;

public class ExtraDetails {
	public static final String MOD_ID = "extra_details";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static boolean enable_door_animation = true;
	public static boolean enable_trap_door_animation = true;

	public static void init() {
		AutoConfig.register(ExtraDetailsConfig.class, GsonConfigSerializer::new);
		BlockEntityRegistry.BLOCK_ENTITIES.register();

		enable_door_animation = getConfig().enable_door_animation;
		enable_trap_door_animation = getConfig().enable_trap_door_animation;

		if (Platform.getEnv().equals(EnvType.CLIENT)) {
			ClientLifecycleEvent.CLIENT_SETUP.register(instance -> client());
		}
	}

	private static void client() {
		BlockEntityRendererRegistry.register(BlockEntityType.SIGN, TiltSignRenderer::new);

		BlockEntityRendererRegistry.register(BlockEntityRegistry.DOOR_ENTITY.get(), DoorRenderer::new);
		BlockEntityRendererRegistry.register(BlockEntityRegistry.TRAP_DOOR_ENTITY.get(), TrapDoorRenderer::new);
	}

	public static ExtraDetailsConfig getConfig() {
		return AutoConfig.getConfigHolder(ExtraDetailsConfig.class).getConfig();
	}
}
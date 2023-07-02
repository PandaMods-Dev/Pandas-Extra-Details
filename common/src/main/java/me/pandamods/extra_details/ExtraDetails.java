package me.pandamods.extra_details;

import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import me.pandamods.extra_details.client.renderer.block.door.DoorRenderer;
import me.pandamods.extra_details.client.renderer.block.door.TrapDoorRenderer;
import me.pandamods.extra_details.client.renderer.block.sign.TiltSignRenderer;
import me.pandamods.extra_details.registries.BlockEntityRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class ExtraDetails {
	public static final String MOD_ID = "extra_details";

	public static void init() {
		AutoConfig.register(ExtraDetailsConfig.class, GsonConfigSerializer::new);
		BlockEntityRegistry.BLOCK_ENTITIES.register();

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
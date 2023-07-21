package me.pandamods.extra_details;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import me.pandamods.extra_details.client.renderer.block.door.DoorRenderer;
import me.pandamods.extra_details.client.renderer.block.door.FenceGateRenderer;
import me.pandamods.extra_details.client.renderer.block.door.TrapDoorRenderer;
import me.pandamods.extra_details.client.renderer.block.redstone.LeverRenderer;
import me.pandamods.extra_details.client.renderer.block.sign.TiltSignRenderer;
import me.pandamods.extra_details.config.ExtraDetailsConfig;
import me.pandamods.extra_details.config.PersistentConfig;
import me.pandamods.extra_details.registries.BlockEntityRegistry;
import me.pandamods.pandalib.PandaLib;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;

public class ExtraDetails {
	public static final String MOD_ID = "extra_details";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static void init() {
		PandaLib.init();

		AutoConfig.register(ExtraDetailsConfig.class, GsonConfigSerializer::new);
		PersistentConfig.init(getConfig());

		BlockEntityRegistry.BLOCK_ENTITIES.register();
		if (Platform.getEnv().equals(EnvType.CLIENT)) {
			ClientLifecycleEvent.CLIENT_SETUP.register(instance -> client());
		}
	}

	private static void client() {
		BlockEntityRendererRegistry.register(BlockEntityType.SIGN, TiltSignRenderer::new);

		BlockEntityRendererRegistry.register(BlockEntityRegistry.DOOR_ENTITY.get(), DoorRenderer::new);
		BlockEntityRendererRegistry.register(BlockEntityRegistry.TRAP_DOOR_ENTITY.get(), TrapDoorRenderer::new);
		BlockEntityRendererRegistry.register(BlockEntityRegistry.FENCE_GATE_ENTITY.get(), context -> new FenceGateRenderer());

		BlockEntityRendererRegistry.register(BlockEntityRegistry.LEVER_ENTITY.get(), context -> new LeverRenderer());
	}

	public static ExtraDetailsConfig getConfig() {
		return AutoConfig.getConfigHolder(ExtraDetailsConfig.class).getConfig();
	}
}
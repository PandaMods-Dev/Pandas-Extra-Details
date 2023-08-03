package me.pandamods.extra_details;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.common.ChunkEvent;
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
import me.pandamods.pandalib.client.render.block.BlockRendererRegistry;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.telemetry.events.WorldLoadEvent;
import net.minecraft.client.telemetry.events.WorldLoadTimesEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;

public class ExtraDetails {
	public static final String MOD_ID = "extra_details";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static void init() {
		if (Platform.getEnv().equals(EnvType.CLIENT)) {
			PandaLib.init();

			AutoConfig.register(ExtraDetailsConfig.class, GsonConfigSerializer::new);
			PersistentConfig.init(getConfig());

			BlockEntityRegistry.BLOCK_ENTITIES.register();
			if (Platform.getEnv().equals(EnvType.CLIENT)) {
				ClientLifecycleEvent.CLIENT_SETUP.register(instance -> client());
			}
		}
	}

	private static void client() {
		BlockEntityRendererRegistry.register(BlockEntityType.SIGN, TiltSignRenderer::new);

		BlockRendererRegistry.register(BlockTags.DOORS, new DoorRenderer());
//		BlockEntityRendererRegistry.register(BlockEntityRegistry.DOOR_ENTITY.get(), DoorRenderer::new);
		BlockEntityRendererRegistry.register(BlockEntityRegistry.TRAP_DOOR_ENTITY.get(), TrapDoorRenderer::new);
		BlockEntityRendererRegistry.register(BlockEntityRegistry.FENCE_GATE_ENTITY.get(), context -> new FenceGateRenderer());

		BlockEntityRendererRegistry.register(BlockEntityRegistry.LEVER_ENTITY.get(), context -> new LeverRenderer());
	}

	public static ExtraDetailsConfig getConfig() {
		return AutoConfig.getConfigHolder(ExtraDetailsConfig.class).getConfig();
	}
}
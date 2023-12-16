package me.pandamods.extra_details;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.client.ClientLifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import me.pandamods.extra_details.api.client.render.block.BlockRendererRegistry;
import me.pandamods.extra_details.client.renderer.block.chest.AnimatedChestRenderer;
import me.pandamods.extra_details.client.renderer.block.chest.AnimatedEnderChestRenderer;
import me.pandamods.extra_details.client.renderer.block.door.DoorRenderer;
import me.pandamods.extra_details.client.renderer.block.door.FenceGateRenderer;
import me.pandamods.extra_details.client.renderer.block.door.TrapDoorRenderer;
import me.pandamods.extra_details.client.renderer.block.redstone.LeverRenderer;
import me.pandamods.extra_details.client.renderer.block.sign.SwingingHangingSignRenderer;
import me.pandamods.extra_details.client.renderer.block.sign.TiltSignRenderer;
import me.pandamods.extra_details.config.ModConfig;
import me.pandamods.extra_details.registries.ClientBlockInit;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.EnvType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;

public class ExtraDetails {
	public static final String MOD_ID = "extra_details";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static void init() {
		if (Platform.getEnv().equals(EnvType.CLIENT)) {
			AutoConfig.register(ModConfig.class, GsonConfigSerializer::new).registerSaveListener((configHolder, extraDetailsConfig) -> {
				Minecraft.getInstance().reloadResourcePacks();
				return InteractionResult.sidedSuccess(true);
			});

			if (Platform.getEnv().equals(EnvType.CLIENT)) {
				ClientLifecycleEvent.CLIENT_SETUP.register(instance -> client());
			}
		}
	}

	private static void client() {
		BlockEntityRendererRegistry.register(BlockEntityType.SIGN, TiltSignRenderer::new);
		BlockEntityRendererRegistry.register(BlockEntityType.ENDER_CHEST, AnimatedEnderChestRenderer::new);
		BlockEntityRendererRegistry.register(BlockEntityType.CHEST, AnimatedChestRenderer::new);
		BlockEntityRendererRegistry.register(BlockEntityType.TRAPPED_CHEST, AnimatedChestRenderer::new);

		ClientBlockInit.init();
		BlockRendererRegistry.register(ClientBlockInit.DOOR, new DoorRenderer());
		BlockRendererRegistry.register(ClientBlockInit.TRAP_DOOR, new TrapDoorRenderer());
		BlockRendererRegistry.register(ClientBlockInit.FENCE_GATE, new FenceGateRenderer());
		BlockRendererRegistry.register(ClientBlockInit.LEVER, new LeverRenderer());
		BlockRendererRegistry.register(ClientBlockInit.HANGING_SIGN, new SwingingHangingSignRenderer());
	}

	public static ModConfig getConfig() {
		return getConfigHolder().getConfig();
	}

	public static ConfigHolder<ModConfig> getConfigHolder() {
		return AutoConfig.getConfigHolder(ModConfig.class);
	}
}
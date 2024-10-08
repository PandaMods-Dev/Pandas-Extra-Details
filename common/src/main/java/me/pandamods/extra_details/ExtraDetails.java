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

package me.pandamods.extra_details;

import com.mojang.logging.LogUtils;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import me.pandamods.extra_details.api.render.BlockRendererRegistry;
import me.pandamods.extra_details.client.renderer.DoorRenderer;
import me.pandamods.extra_details.client.renderer.FenceGateRenderer;
import me.pandamods.extra_details.client.renderer.LeverRenderer;
import me.pandamods.extra_details.client.renderer.TrapDoorRenderer;
import me.pandamods.extra_details.client.renderer.sign.TiltSignRenderer;
import me.pandamods.extra_details.config.EDConfig;
import me.pandamods.pandalib.api.config.PandaLibConfig;
import me.pandamods.pandalib.api.config.holders.ConfigHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;

public class ExtraDetails {
	public static final String MOD_ID = "extra_details";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final ExtraDetailsLevelRenderer LEVEL_RENDERER = new ExtraDetailsLevelRenderer();
	public static final ConfigHolder<EDConfig> CONFIG = PandaLibConfig.registerClient(EDConfig.class);

	public static void init() {
		if (CONFIG.get().blockSettings.sign.enabled)
			BlockEntityRendererRegistry.register(BlockEntityType.SIGN, TiltSignRenderer::new);

		// Todo make renders registration not be hardcoded, maybe something like how block models are registered and handled.
		BlockRendererRegistry.register(new LeverRenderer(), Blocks.LEVER, blockState -> CONFIG.get().blockSettings.lever.enabled);

		BlockRendererRegistry.register(new DoorRenderer(), blockState -> CONFIG.get().blockSettings.door.enabled,
				Blocks.OAK_DOOR, Blocks.DARK_OAK_DOOR, Blocks.ACACIA_DOOR, Blocks.BAMBOO_DOOR, Blocks.BIRCH_DOOR, Blocks.CHERRY_DOOR, Blocks.CRIMSON_DOOR,
				Blocks.WARPED_DOOR, Blocks.JUNGLE_DOOR, Blocks.SPRUCE_DOOR, Blocks.MANGROVE_DOOR, Blocks.IRON_DOOR);

		BlockRendererRegistry.register(new TrapDoorRenderer(), blockState -> CONFIG.get().blockSettings.trapdoor.enabled,
				Blocks.OAK_TRAPDOOR, Blocks.DARK_OAK_TRAPDOOR, Blocks.ACACIA_TRAPDOOR, Blocks.BAMBOO_TRAPDOOR, Blocks.BIRCH_TRAPDOOR, Blocks.CHERRY_TRAPDOOR,
				Blocks.CRIMSON_TRAPDOOR, Blocks.WARPED_TRAPDOOR, Blocks.JUNGLE_TRAPDOOR, Blocks.SPRUCE_TRAPDOOR, Blocks.MANGROVE_TRAPDOOR, Blocks.IRON_TRAPDOOR);

		BlockRendererRegistry.register(new FenceGateRenderer(), blockState -> CONFIG.get().blockSettings.fenceGate.enabled,
				Blocks.OAK_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.BAMBOO_FENCE_GATE, Blocks.BIRCH_FENCE_GATE,
				Blocks.CHERRY_FENCE_GATE, Blocks.CRIMSON_FENCE_GATE, Blocks.WARPED_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE,
				Blocks.MANGROVE_FENCE_GATE);
	}

	public static ResourceLocation LOCATION(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
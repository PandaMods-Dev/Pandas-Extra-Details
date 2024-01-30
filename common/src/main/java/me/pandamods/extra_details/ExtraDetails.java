package me.pandamods.extra_details;

import com.mojang.logging.LogUtils;
import me.pandamods.extra_details.api.client.render.block.BlockRendererRegistry;
import me.pandamods.extra_details.client.renderer.LeverRenderer;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;

public class ExtraDetails {
	public static final String MOD_ID = "extra_details";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static void init() {
	}

	public static void client() {
		BlockRendererRegistry.register(Blocks.LEVER, LeverRenderer::new);
	}
}
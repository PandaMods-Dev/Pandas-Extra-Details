package me.pandamods.extra_details;

import com.mojang.logging.LogUtils;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import me.pandamods.extra_details.api.client.render.block.BlockRendererDispatcher;
import me.pandamods.extra_details.api.client.render.block.BlockRendererRegistry;
import me.pandamods.extra_details.client.renderer.LeverRenderer;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;

public class ExtraDetails {
	public static final String MOD_ID = "extra_details";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final BlockRendererDispatcher BLOCK_RENDERER_DISPATCHER = new BlockRendererDispatcher();

	public static final Event<BlockRendererRegistryEvent> blockRendererRegistryEvent = EventFactory.createLoop();

	public static void init() {
	}

	public static void client() {
		blockRendererRegistryEvent.register(registry -> {
			registry.register(Blocks.LEVER, new LeverRenderer());
		});
	}

	public interface BlockRendererRegistryEvent {
		void register(BlockRendererRegistry registry);
	}
}
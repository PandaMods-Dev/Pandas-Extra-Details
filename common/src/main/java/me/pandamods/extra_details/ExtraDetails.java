package me.pandamods.extra_details;

import com.mojang.logging.LogUtils;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import me.pandamods.extra_details.api.render.BlockRendererRegistry;
import me.pandamods.extra_details.client.renderer.LeverRenderer;
import me.pandamods.pandalib.client.render.PLInternalShaders;
import me.pandamods.pandalib.resource.AssimpResources;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;

public class ExtraDetails {
	public static final String MOD_ID = "extra_details";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final AssimpResources ASSIMP_RESOURCES = new AssimpResources();
	public static final ExtraDetailsLevelRenderer LEVEL_RENDERER = new ExtraDetailsLevelRenderer();

	public static void init() {
		ClientReloadShadersEvent.EVENT.register(PLInternalShaders::register);
		ReloadListenerRegistry.register(PackType.CLIENT_RESOURCES, ExtraDetails.ASSIMP_RESOURCES,
				new ResourceLocation(MOD_ID, "assimp_loader"));

		BlockRendererRegistry.register(new LeverRenderer(), Blocks.LEVER);
	}
}
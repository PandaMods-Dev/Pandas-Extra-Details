package me.pandamods.extra_details.registries;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntityRegistry;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntityType;
import me.pandamods.extra_details.client.clientblockentity.DoorBlockEntity;
import me.pandamods.extra_details.client.clientblockentity.LeverBlockEntity;
import me.pandamods.extra_details.client.renderer.DoorRenderer;
import me.pandamods.extra_details.client.renderer.LeverRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

public class ClientBlockEntityRegistries {
	public static void init() {
		ExtraDetails.rendererRegistryEvent.register(registry -> {
			registry.register(ClientBlockEntityRegistries.DOOR, new DoorRenderer());
			registry.register(ClientBlockEntityRegistries.LEVER, new LeverRenderer());
		});
	}

	public static final ClientBlockEntityType<?> DOOR = ClientBlockEntityRegistry
			.register(new ResourceLocation(ExtraDetails.MOD_ID, "door"), ClientBlockEntityType
					.Builder.of(DoorBlockEntity::new, Blocks.OAK_DOOR).hideBase());

	public static final ClientBlockEntityType<?> LEVER = ClientBlockEntityRegistry
			.register(new ResourceLocation(ExtraDetails.MOD_ID, "lever"), ClientBlockEntityType
			.Builder.of(LeverBlockEntity::new, Blocks.LEVER).hideBase());
}

package me.pandamods.extra_details.registries;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntityRegistry;
import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntityType;
import me.pandamods.extra_details.api.client.render.block.ClientBlockEntityRenderer;
import me.pandamods.extra_details.client.clientblockentity.LeverBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;

public class ClientBlockEntityRegistries {
	public static void init() {}

	public static final ClientBlockEntityType<?> LEVER = ClientBlockEntityRegistry
			.register(new ResourceLocation(ExtraDetails.MOD_ID, "lever"), ClientBlockEntityType
			.Builder.of(LeverBlockEntity::new, Blocks.LEVER));
}

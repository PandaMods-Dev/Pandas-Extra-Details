package me.pandamods.extra_details.api.client.clientblockentity;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ClientBlockEntityRegistry {
	private static final Logger LOGGER = LogUtils.getLogger();
	public static Map<ResourceLocation, ClientBlockEntityType<?>> REGISTRIES = new HashMap<>();

	public static <T extends ClientBlockEntity> ClientBlockEntityType<T> register(ResourceLocation resourceLocation,
																				   ClientBlockEntityType.Builder<T> builder) {
		if (builder.validBlocks.isEmpty()) {
			LOGGER.warn("Client Block entity type {} requires at least one valid block to be defined!", resourceLocation);
		}
		ClientBlockEntityType<T> blockEntityType = builder.build();
		REGISTRIES.put(resourceLocation, blockEntityType);
		return blockEntityType;
	}

	public static ClientBlockEntityType<?> get(BlockState blockState) {
		for (ClientBlockEntityType<?> value : REGISTRIES.values()) {
			if (value.isValid(blockState)) {
				return value;
			}
		}
		return null;
	}
}

package me.pandamods.extra_details.api.extensions;

import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

public interface LevelChunkExtension {
	default Map<BlockPos, ClientBlockEntity> getClientBlockEntities() {
		return new HashMap<>();
	}
	default void setClientBlockEntity(ClientBlockEntity clientBlockEntity) {}
	default void removeClientBlockEntity(BlockPos blockPos) {}
	default ClientBlockEntity getClientBlockEntity(BlockPos blockPos) {
		return null;
	}
}

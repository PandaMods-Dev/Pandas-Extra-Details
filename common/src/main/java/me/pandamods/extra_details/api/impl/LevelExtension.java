package me.pandamods.extra_details.api.impl;

import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntity;
import net.minecraft.core.BlockPos;

import java.util.Map;

public interface LevelExtension {
	default void setClientBlockEntity(ClientBlockEntity clientBlockEntity) {}
	default void removeClientBlockEntity(BlockPos blockPos) {}
}

package me.pandamods.extra_details.api.impl;

import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import net.minecraft.core.BlockPos;

public interface LevelExtension {
	default void setClientBlockEntity(ClientBlockEntity clientBlockEntity) {}
	default void removeClientBlockEntity(BlockPos blockPos) {}
}

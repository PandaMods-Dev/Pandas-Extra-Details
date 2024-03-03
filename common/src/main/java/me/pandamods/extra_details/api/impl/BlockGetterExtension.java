package me.pandamods.extra_details.api.impl;

import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntity;
import net.minecraft.core.BlockPos;

public interface BlockGetterExtension {
	default ClientBlockEntity getClientBlockEntity(BlockPos blockPos) {
		return null;
	}
}

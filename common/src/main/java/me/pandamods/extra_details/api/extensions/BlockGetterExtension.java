package me.pandamods.extra_details.api.extensions;

import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import net.minecraft.core.BlockPos;

public interface BlockGetterExtension {
	default ClientBlockEntity getClientBlockEntity(BlockPos blockPos) {
		return null;
	}
}

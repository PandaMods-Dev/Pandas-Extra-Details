package me.pandamods.extra_details.api.impl;

import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntity;

import java.util.ArrayList;
import java.util.List;

public interface CompiledChunkExtension {
    default List<ClientBlockEntity> getClientBlockEntities() {
		return new ArrayList<>();
	}
}
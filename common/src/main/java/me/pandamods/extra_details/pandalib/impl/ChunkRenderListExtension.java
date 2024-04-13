package me.pandamods.extra_details.pandalib.impl;

import me.jellysquid.mods.sodium.client.util.iterator.ByteIterator;

public interface ChunkRenderListExtension {
	ByteIterator extraDetails$sectionsWithClientBlocksIterator();
	int extraDetails$getSectionsWithClientBlocksCount();
}

package me.pandamods.extra_details.api.extensions;

import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;

public interface ClonedChunkSectionExtension {
	default Int2ReferenceMap<ClientBlockEntity> getClientBlockEntityMap() {
		return null;
	}
}

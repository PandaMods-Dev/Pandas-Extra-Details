package me.pandamods.extra_details.pandalib.entity;

import me.pandamods.extra_details.pandalib.cache.ObjectCache;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface MeshAnimatable {
	ObjectCache getCache();
}

package me.pandamods.pandalib.entity;

import me.pandamods.pandalib.cache.ObjectCache;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface MeshAnimatable {
	ObjectCache getCache();
}

package me.pandamods.pandalib.entity;

import me.pandamods.pandalib.cache.MeshCache;
import me.pandamods.pandalib.client.animation_controller.AnimationController;
import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface MeshAnimatable {
	MeshCache getCache();
}

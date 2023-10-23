package me.pandamods.pandalib.client.animation_controller;

import me.pandamods.pandalib.entity.MeshAnimatable;

public interface AnimationControllerProvider<T extends MeshAnimatable> {
	AnimationController<T> create(T base);
}

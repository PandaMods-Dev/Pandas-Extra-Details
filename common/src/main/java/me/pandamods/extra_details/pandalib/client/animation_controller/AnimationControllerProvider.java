package me.pandamods.extra_details.pandalib.client.animation_controller;

import me.pandamods.extra_details.pandalib.entity.MeshAnimatable;

public interface AnimationControllerProvider<T extends MeshAnimatable> {
	AnimationController<T> create(T base);
}

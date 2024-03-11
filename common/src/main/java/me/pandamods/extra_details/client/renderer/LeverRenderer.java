package me.pandamods.extra_details.client.renderer;

import me.pandamods.extra_details.api.clientblockentity.renderer.MeshClientBlockRenderer;
import me.pandamods.extra_details.client.animationcontroller.LeverAnimationController;
import me.pandamods.extra_details.client.clientblockentity.LeverBlockEntity;
import me.pandamods.extra_details.client.model.LeverModel;

public class LeverRenderer extends MeshClientBlockRenderer<LeverBlockEntity, LeverModel, LeverAnimationController> {
	public LeverRenderer() {
		super(new LeverModel(), new LeverAnimationController());
	}
}

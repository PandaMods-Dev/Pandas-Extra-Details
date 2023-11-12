package me.pandamods.extra_details.client.renderer.block.door;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.model.block.door.DoorModel;
import me.pandamods.extra_details.entity.block.DoorClientBlock;
import me.pandamods.pandalib.client.render.block.extensions.MeshClientBlockRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class DoorRenderer extends MeshClientBlockRenderer<DoorClientBlock, DoorModel> {
	public DoorRenderer() {
		super(new DoorModel());
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public boolean enabled(BlockState state) {
		return ExtraDetails.getConfig().blockSettings.door.enabled && ExtraDetails.getConfig().isAllowed(state.getBlock());
	}
}

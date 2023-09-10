package me.pandamods.extra_details.client.renderer.block.door;

import me.pandamods.extra_details.client.model.block.TrapDoorModel;
import me.pandamods.extra_details.entity.block.TrapDoorClientBlock;
import me.pandamods.pandalib.client.render.block.extensions.MeshClientBlockRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class TrapDoorRenderer extends MeshClientBlockRenderer<TrapDoorClientBlock, TrapDoorModel> {
	public TrapDoorRenderer() {
		super(new TrapDoorModel());
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}
}

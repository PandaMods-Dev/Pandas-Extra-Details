package me.pandamods.extra_details.client.renderer.block.door;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.client.model.block.DoorModel;
import me.pandamods.extra_details.client.model.block.TrapDoorModel;
import me.pandamods.extra_details.entity.block.DoorClientBlock;
import me.pandamods.extra_details.entity.block.TrapDoorClientBlock;
import me.pandamods.extra_details.utils.animation.CurveRamp;
import me.pandamods.extra_details.utils.animation.KeyPoint;
import me.pandamods.extra_details.utils.animation.KeyType;
import me.pandamods.pandalib.client.render.block.extensions.MeshClientBlockRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import org.joml.Vector2f;

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

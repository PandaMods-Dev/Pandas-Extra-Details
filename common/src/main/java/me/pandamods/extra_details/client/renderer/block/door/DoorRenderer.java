package me.pandamods.extra_details.client.renderer.block.door;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.client.model.block.DoorModel;
import me.pandamods.extra_details.entity.block.DoorClientBlock;
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
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector2f;

@Environment(EnvType.CLIENT)
public class DoorRenderer extends MeshClientBlockRenderer<DoorClientBlock, DoorModel> {
	public DoorRenderer() {
		super(new DoorModel());
	}

	public static final CurveRamp doorAnimation = new CurveRamp(
			new KeyPoint(KeyType.CATMULL_ROM, new Vector2f(0, 0)),
			new KeyPoint(KeyType.LINEAR, new Vector2f(0.5f, 1)),
			new KeyPoint(KeyType.CATMULL_ROM, new Vector2f(0.75f, 0.95f)),
			new KeyPoint(KeyType.LINEAR, new Vector2f(1, 1))
	);

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}
}

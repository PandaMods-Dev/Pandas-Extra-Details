package me.pandamods.extra_details.client.renderer.block.door;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.model.block.door.TrapDoorModel;
import me.pandamods.extra_details.entity.block.TrapDoorClientBlock;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.render.block.extensions.MeshClientBlockRenderer;
import me.pandamods.pandalib.utils.RenderUtils;
import me.pandamods.pandalib.utils.VectorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import org.joml.Math;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class TrapDoorRenderer extends MeshClientBlockRenderer<TrapDoorClientBlock, TrapDoorModel> {
	public TrapDoorRenderer() {
		super(new TrapDoorModel());
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public boolean enabled(BlockState state) {
		return ExtraDetails.getConfig().blockSettings.trapdoor.enabled && ExtraDetails.getConfig().isAllowed(state.getBlock());
	}

	@Override
	public void render(TrapDoorClientBlock block, PoseStack poseStack, MultiBufferSource buffer, int lightColor, int overlay, float partialTick) {
		this.renderRig(block, this.model, poseStack, buffer, lightColor, overlay, false);

		BlockState blockState = block.getBlockState().setValue(TrapDoorBlock.FACING, Direction.NORTH).setValue(TrapDoorBlock.OPEN, false)
				.setValue(TrapDoorBlock.HALF, Half.BOTTOM);

		poseStack.pushPose();
		if (block.getBlockState().getValue(TrapDoorBlock.HALF).equals(Half.TOP))
			poseStack.translate(0, 13f / 16, 0);

		Armature armature = block.getCache().armature;
		if (armature != null) {
			VectorUtils.rotateByPivot(poseStack, new Vector3f(.5f, 0, .5f),
					new Vector3f(0, Math.toRadians(this.getYRotation(block.getBlockState())), 0));
			armature.getBone("door").ifPresent(bone -> bone.applyToPoseStack(poseStack));

			RenderUtils.renderBlock(poseStack, blockState, block.getBlockPos(), block.getLevel(),
					buffer.getBuffer(ItemBlockRenderTypes.getRenderType(blockState, false)), lightColor, overlay);

		}
		poseStack.popPose();
	}
}

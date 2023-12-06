package me.pandamods.extra_details.client.renderer.block.door;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.model.block.door.DoorModel;
import me.pandamods.extra_details.entity.block.DoorClientBlock;
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
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.core.Direction;
import net.minecraft.util.datafix.fixes.ChunkPalettedStorageFix;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.joml.Math;
import org.joml.Vector3f;

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

	@Override
	public void render(DoorClientBlock block, PoseStack poseStack, MultiBufferSource buffer, int lightColor, int overlay, float partialTick) {
		if (block.getBlockState().getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER) {
			this.renderRig(block, this.model, poseStack, buffer, lightColor, overlay);

			BlockState blockState = block.getBlockState().setValue(DoorBlock.FACING, Direction.NORTH).setValue(DoorBlock.OPEN, false)
					.setValue(DoorBlock.HALF, DoubleBlockHalf.LOWER);

			poseStack.pushPose();
			Armature armature = block.getCache().armature;
			if (armature != null) {
				VectorUtils.rotateByPivot(poseStack, new Vector3f(.5f, 0, .5f),
						new Vector3f(0, Math.toRadians(this.getYRotation(block.getBlockState()) + 180), 0));
				armature.getBone("door").ifPresent(bone -> bone.applyToPoseStack(poseStack));

				RenderUtils.renderBlock(poseStack, blockState, block.getBlockPos(), block.getLevel(),
						buffer.getBuffer(ItemBlockRenderTypes.getRenderType(blockState, false)), lightColor, overlay);

				poseStack.translate(0, 1, 0);

				RenderUtils.renderBlock(poseStack, blockState.setValue(DoorBlock.HALF, DoubleBlockHalf.UPPER),
						block.getBlockPos(), block.getLevel(),
						buffer.getBuffer(ItemBlockRenderTypes.getRenderType(blockState, false)), lightColor, overlay);
			}
			poseStack.popPose();
		}
	}
}

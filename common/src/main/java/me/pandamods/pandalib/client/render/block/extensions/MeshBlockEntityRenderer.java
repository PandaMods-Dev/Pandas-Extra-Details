package me.pandamods.pandalib.client.render.block.extensions;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.client.render.MeshRenderer;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

@Environment(EnvType.CLIENT)
public abstract class MeshBlockEntityRenderer<T extends BlockEntity & MeshAnimatable, M extends MeshModel<T>>
		implements MeshRenderer<T, M>, BlockEntityRenderer<T> {
	private final M model;

	public MeshBlockEntityRenderer(BlockEntityRendererProvider.Context context, M model) {
		this.model = model;
	}


	@Override
	public void render(T blockEntity, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
		stack.pushPose();
		translateBlock(blockEntity.getBlockState(), stack);
		this.renderMesh(blockEntity, model, stack, buffer, packedLight, packedOverlay);
		stack.popPose();
	}
}

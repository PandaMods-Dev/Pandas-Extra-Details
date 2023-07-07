package me.pandamods.extra_details.client.renderer.block.redstone;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.pandamods.extra_details.client.model.block.LeverModel;
import me.pandamods.extra_details.entity.block.LeverEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class LeverRenderer extends GeoBlockRenderer<LeverEntity> {
	public LeverRenderer() {
		super(new LeverModel());
	}

	@Override
	public void renderRecursively(PoseStack poseStack, LeverEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		buffer = bufferSource.getBuffer(getRenderType(animatable,
				((LeverModel)this.model).getTextureResource(animatable, !bone.getName().equalsIgnoreCase("root")),
				bufferSource, partialTick));
		super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay,
				red, green, blue, alpha);
	}

	@Override
	public ResourceLocation getTextureLocation(LeverEntity animatable) {
		return super.getTextureLocation(animatable);
	}
}
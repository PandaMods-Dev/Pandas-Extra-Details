package me.pandamods.extra_details.client.model.block.sign;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.HangingSignClientBlock;
import me.pandamods.extra_details.pandalib.client.model.Armature;
import me.pandamods.extra_details.pandalib.client.model.MeshModel;
import me.pandamods.extra_details.pandalib.utils.RandomUtils;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;

import java.util.Map;

public class HangingSignModel implements MeshModel<HangingSignClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(HangingSignClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/sign/hanging_sign.json");
	}

	@Override
	public @Nullable ResourceLocation getArmatureLocation(HangingSignClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/armatures/block/sign/hanging_sign.json");
	}

	@Override
	public Map<String, ResourceLocation> getTextureLocations(HangingSignClientBlock base) {
		BlockState blockState = base.getBlockState();
		SignBlock signBlock = (SignBlock)blockState.getBlock();
		WoodType woodType = SignBlock.getWoodType(signBlock);
		return Map.of("", new ResourceLocation("textures/" + Sheets.getHangingSignMaterial(woodType).texture().getPath() + ".png"));
	}

	@Override
	public void setupAnim(HangingSignClientBlock base, Armature armature, float deltaSeconds) {
		evaluateVisibleObjects(armature, base.getBlockState());

		BlockState stateBelow = base.getLevel().getBlockState(base.getBlockPos().below());
		if (!(stateBelow.is(BlockTags.ALL_HANGING_SIGNS) &&
				stateBelow.hasProperty(CeilingHangingSignBlock.ATTACHED) && stateBelow.getValue(CeilingHangingSignBlock.ATTACHED))) {
			armature.getBone("chains").ifPresent(bone -> {
				bone.localTransform(matrix -> matrix.setRotationXYZ(
						Math.toRadians(
								Math.cos((base.animTime += deltaSeconds) + RandomUtils.randomFloatFromBlockPos(base.getBlockPos())) * 5
						),
						0,
						0
				));
			});
		} else {
			base.animTime = 0;
		}
	}

	public void evaluateVisibleObjects(Armature armature, BlockState state) {
		boolean isCeiling = state.getBlock() instanceof CeilingHangingSignBlock;
		armature.setHidden("short_chains", "v_chains", "long_chains");
		armature.setVisibility(!isCeiling, "plank", "short_chains");
		if (isCeiling) {
			boolean isAttached = state.getValue(BlockStateProperties.ATTACHED);
			armature.setVisibility(!isAttached, "long_chains");
			armature.setVisibility(isAttached, "v_chains");
		}
	}
}

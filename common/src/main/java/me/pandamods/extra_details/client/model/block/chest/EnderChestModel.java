package me.pandamods.extra_details.client.model.block.chest;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.animation_controller.block.chest.EnderChestAnimationController;
import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public class EnderChestModel implements MeshModel<EnderChestBlockEntity> {
	@Override
	public ResourceLocation getMeshLocation(EnderChestBlockEntity base) {
		BlockState blockState = base.getBlockState();
		ChestType chestType = blockState.hasProperty(ChestBlock.TYPE) ? blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
		return switch (chestType) {
			case SINGLE -> new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/chest/chest.json");
			case LEFT -> new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/chest/chest_left.json");
			case RIGHT -> new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/chest/chest_right.json");
		};
	}

	@Override
	public ResourceLocation getTextureLocation(String textureName, EnderChestBlockEntity base) {
		return new ResourceLocation("textures/entity/chest/normal.png");
	}

	@Override
	public AnimationControllerProvider<EnderChestBlockEntity> createAnimationController() {
		return EnderChestAnimationController::new;
	}
}

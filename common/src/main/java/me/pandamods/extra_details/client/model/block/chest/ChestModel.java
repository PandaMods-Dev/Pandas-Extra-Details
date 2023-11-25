package me.pandamods.extra_details.client.model.block.chest;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.animation_controller.block.chest.ChestAnimationController;
import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public class ChestModel<T extends MeshAnimatable> implements MeshModel<T> {
	@Override
	public ResourceLocation getMeshLocation(T base) {
		if (base instanceof BlockEntity block) {
			BlockState blockState = block.getBlockState();
			ChestType chestType = blockState.hasProperty(ChestBlock.TYPE) ? blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
			return switch (chestType) {
				case SINGLE -> new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/chest/chest.json");
				case LEFT -> new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/chest/chest_left.json");
				case RIGHT -> new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/chest/chest_right.json");
			};
		}
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/chest/chest.json");
	}

	@Override
	public ResourceLocation getTextureLocation(String textureName, T base) {
		return new ResourceLocation("textures/entity/chest/normal.png");
	}

	@Override
	public AnimationControllerProvider<T> createAnimationController() {
		return ChestAnimationController::new;
	}
}

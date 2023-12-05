package me.pandamods.extra_details.client.model.block.door;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.animation_controller.block.door.DoorAnimationController;
import me.pandamods.extra_details.entity.block.DoorClientBlock;
import me.pandamods.extra_details.entity.block.HangingSignClientBlock;
import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.client.render.block.ClientBlockRenderDispatcher;
import me.pandamods.pandalib.utils.RandomUtils;
import me.pandamods.pandalib.utils.RenderUtils;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import org.joml.Math;

import java.util.List;

public class DoorModel implements MeshModel<DoorClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(DoorClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/door/door.json");
	}

	@Override
	public ResourceLocation getTextureLocation(String textureName, DoorClientBlock base) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(base.getBlockState());
		ResourceLocation resourceLocation = textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
	}

	@Override
	public AnimationControllerProvider<DoorClientBlock> createAnimationController(DoorClientBlock base) {
		return DoorAnimationController::new;
	}

	@Override
	public void setPropertiesOnCreation(DoorClientBlock base, Armature armature) {
		boolean mirror = base.getBlockState().getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
		armature.mirrorX(mirror, false, false);
		armature.mirrorY(false, mirror, false);
	}
}

package me.pandamods.extra_details.client.model.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.renderer.block.door.DoorRenderer;
import me.pandamods.extra_details.entity.block.TrapDoorClientBlock;
import me.pandamods.pandalib.utils.RenderUtils;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.MeshModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import org.joml.Math;

import java.util.List;

public class TrapDoorModel implements MeshModel<TrapDoorClientBlock> {
	@Override
	public ResourceLocation getMeshLocation(TrapDoorClientBlock base) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/door/trap_door.json");
	}

	@Override
	public ResourceLocation getTextureLocation(String textureName, TrapDoorClientBlock base) {
		List<ResourceLocation> textures = RenderUtils.getBlockTextures(base.getBlockState());
		ResourceLocation resourceLocation = textures.get(0);
		if (resourceLocation.getPath().endsWith(".png"))
			return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath());
		return new ResourceLocation(resourceLocation.getNamespace(), "textures/" + resourceLocation.getPath() + ".png");
	}

	@Override
	public void setupAnim(TrapDoorClientBlock base, Armature armature, float deltaSeconds) {
		BlockState state = base.getBlockState();

		float speed = deltaSeconds / ExtraDetails.getConfig().trap_door_animation_length;
		base.animTime = Math.clamp(0, 1, base.animTime + (state.getValue(TrapDoorBlock.OPEN) ? speed : -speed));

		float animValue = state.getValue(TrapDoorBlock.OPEN) ?
				DoorRenderer.doorAnimation.getValue(base.animTime) :
				1 - DoorRenderer.doorAnimation.getValue(1 - base.animTime);

		armature.getBone("door").ifPresent(bone -> {
			boolean isTop = state.getValue(TrapDoorBlock.HALF).equals(Half.TOP);
			if (isTop)
				bone.setTranslation(0, (float) 13 / 16, 0);
			bone.setRotation(Math.toRadians(
					Math.lerp(0, isTop ? 90 : -90, animValue)), 0, 0);
		});
	}
}

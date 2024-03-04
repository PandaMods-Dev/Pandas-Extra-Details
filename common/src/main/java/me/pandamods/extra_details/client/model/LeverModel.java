package me.pandamods.extra_details.client.model;

import com.google.common.collect.ImmutableMap;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.client.clientblockentity.LeverBlockEntity;
import me.pandamods.pandalib.client.Model;
import me.pandamods.pandalib.client.armature.Armature;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class LeverModel implements Model<LeverBlockEntity> {
	@Override
	public ResourceLocation modelLocation(LeverBlockEntity leverBlockEntity) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/debug2.json");
	}

	@Override
	public Map<String, ResourceLocation> textureLocation(LeverBlockEntity leverBlockEntity) {
		return ImmutableMap.of(
				"", new ResourceLocation("textures/block/cobblestone.png"),
				"lever", new ResourceLocation("textures/block/lever.png")
		);
	}
}

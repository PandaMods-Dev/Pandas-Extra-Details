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
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/redstone/lever.json");
	}

	@Override
	public ResourceLocation textureLocation(LeverBlockEntity leverBlockEntity, String name) {
		if (name.equals("lever")) return new ResourceLocation("textures/block/lever.png");
		return new ResourceLocation("textures/block/cobblestone.png");
	}
}

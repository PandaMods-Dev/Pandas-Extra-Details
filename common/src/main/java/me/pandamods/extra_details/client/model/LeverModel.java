package me.pandamods.extra_details.client.model;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.pandalib.client.Model;
import net.minecraft.resources.ResourceLocation;

public class LeverModel implements Model {
	@Override
	public ResourceLocation modelLocation() {
		return new ResourceLocation(ExtraDetails.MOD_ID, "geo/block/lever.geo.json");
	}

	@Override
	public ResourceLocation textureLocation() {
		return new ResourceLocation("textures/block/cobblestone.png");
	}
}

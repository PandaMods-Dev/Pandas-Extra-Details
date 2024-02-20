package me.pandamods.extra_details.client.model;

import com.google.common.collect.ImmutableMap;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.api.client.render.block.BlockContext;
import me.pandamods.pandalib.client.Model;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class LeverModel implements Model<BlockContext> {
	@Override
	public ResourceLocation modelLocation(BlockContext blockContext) {
		return new ResourceLocation(ExtraDetails.MOD_ID, "pandalib/meshes/block/redstone/lever.json");
	}

	@Override
	public Map<String, ResourceLocation> textureLocation(BlockContext blockContext) {
		return ImmutableMap.of(
				"", new ResourceLocation("textures/block/cobblestone.png"),
				"lever", new ResourceLocation("textures/block/lever.png")
		);
	}
}

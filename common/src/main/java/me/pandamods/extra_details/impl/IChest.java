package me.pandamods.extra_details.impl;

import me.pandamods.pandalib.entity.MeshAnimatable;
import net.minecraft.world.level.block.entity.ChestLidController;

public interface IChest extends MeshAnimatable {
	ChestLidController extraDetails$getChestLidController();
}

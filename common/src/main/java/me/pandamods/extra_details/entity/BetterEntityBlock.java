package me.pandamods.extra_details.entity;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;

public interface BetterEntityBlock extends EntityBlock {
	Class<?> validBlockClass();
}

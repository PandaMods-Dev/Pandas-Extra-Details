package me.pandamods.extra_details.registries;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.*;
import me.pandamods.pandalib.client.render.block.ClientBlockType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

public class ClientBlockRegistry {
	public static void init() { }

	public static ClientBlockType<DoorClientBlock> DOOR = me.pandamods.pandalib.client.render.block.ClientBlockRegistry.register(new ResourceLocation(ExtraDetails.MOD_ID, "door"),
			new ClientBlockType.Builder<DoorClientBlock>(DoorClientBlock::new).validBlockTags(BlockTags.DOORS).build());

	public static ClientBlockType<TrapDoorClientBlock> TRAP_DOOR = me.pandamods.pandalib.client.render.block.ClientBlockRegistry.register(new ResourceLocation(ExtraDetails.MOD_ID, "trap_door"),
			new ClientBlockType.Builder<TrapDoorClientBlock>(TrapDoorClientBlock::new).validBlockTags(BlockTags.TRAPDOORS).build());

	public static ClientBlockType<FenceGateClientBlock> FENCE_GATE = me.pandamods.pandalib.client.render.block.ClientBlockRegistry.register(new ResourceLocation(ExtraDetails.MOD_ID, "fence_gate"),
			new ClientBlockType.Builder<FenceGateClientBlock>(FenceGateClientBlock::new).validBlockTags(BlockTags.FENCE_GATES).build());

	public static ClientBlockType<LeverClientBlock> LEVER = me.pandamods.pandalib.client.render.block.ClientBlockRegistry.register(new ResourceLocation(ExtraDetails.MOD_ID, "lever"),
			new ClientBlockType.Builder<LeverClientBlock>(LeverClientBlock::new).validBlocks(Blocks.LEVER).build());

	public static ClientBlockType<HangingSignClientBlock> HANGING_SIGN = me.pandamods.pandalib.client.render.block.ClientBlockRegistry.register(new ResourceLocation(ExtraDetails.MOD_ID, "hanging_sign"),
			new ClientBlockType.Builder<HangingSignClientBlock>(HangingSignClientBlock::new).validBlockTags(BlockTags.ALL_HANGING_SIGNS).build());
}

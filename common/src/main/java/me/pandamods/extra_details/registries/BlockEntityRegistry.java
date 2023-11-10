package me.pandamods.extra_details.registries;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.*;
import me.pandamods.pandalib.client.render.block.ClientBlockRegistry;
import me.pandamods.pandalib.client.render.block.ClientBlockType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

public class BlockEntityRegistry {
	public static void init() { }

	public static ClientBlockType<DoorClientBlock> DOOR = ClientBlockRegistry.register(new ResourceLocation(ExtraDetails.MOD_ID, "door"),
			new ClientBlockType.Builder<DoorClientBlock>(DoorClientBlock::new).validBlockTags(BlockTags.DOORS).build());

	public static ClientBlockType<TrapDoorClientBlock> TRAP_DOOR = ClientBlockRegistry.register(new ResourceLocation(ExtraDetails.MOD_ID, "trap_door"),
			new ClientBlockType.Builder<TrapDoorClientBlock>(TrapDoorClientBlock::new).validBlockTags(BlockTags.TRAPDOORS).build());

	public static ClientBlockType<FenceGateClientBlock> FENCE_GATE = ClientBlockRegistry.register(new ResourceLocation(ExtraDetails.MOD_ID, "fence_gate"),
			new ClientBlockType.Builder<FenceGateClientBlock>(FenceGateClientBlock::new).validBlockTags(BlockTags.FENCE_GATES).build());

	public static ClientBlockType<LeverClientBlock> LEVER = ClientBlockRegistry.register(new ResourceLocation(ExtraDetails.MOD_ID, "lever"),
			new ClientBlockType.Builder<LeverClientBlock>(LeverClientBlock::new).validBlocks(Blocks.LEVER).build());
}

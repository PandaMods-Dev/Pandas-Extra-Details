package me.pandamods.extra_details.registries;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.DoorClientBlock;
import me.pandamods.extra_details.entity.block.FenceGateClientBlock;
import me.pandamods.extra_details.entity.block.LeverClientBlock;
import me.pandamods.extra_details.entity.block.TrapDoorClientBlock;
import me.pandamods.pandalib.client.render.block.ClientBlockRegistry;
import me.pandamods.pandalib.client.render.block.ClientBlockType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class BlockEntityRegistry {
	public static void init() { }

	public static ClientBlockType<DoorClientBlock> DOOR = ClientBlockRegistry.register(new ResourceLocation(ExtraDetails.MOD_ID, "door"),
			new ClientBlockType.Builder<DoorClientBlock>(DoorClientBlock::new).validBlocks(Blocks.OAK_DOOR).build());

	public static ClientBlockType<TrapDoorClientBlock> TRAP_DOOR = ClientBlockRegistry.register(new ResourceLocation(ExtraDetails.MOD_ID, "trap_door"),
			new ClientBlockType.Builder<TrapDoorClientBlock>(TrapDoorClientBlock::new).validBlocks(Blocks.OAK_TRAPDOOR).build());

	public static ClientBlockType<FenceGateClientBlock> FENCE_GATE = ClientBlockRegistry.register(new ResourceLocation(ExtraDetails.MOD_ID, "fence_gate"),
			new ClientBlockType.Builder<FenceGateClientBlock>(FenceGateClientBlock::new).validBlocks(Blocks.OAK_FENCE_GATE).build());

	public static ClientBlockType<LeverClientBlock> LEVER = ClientBlockRegistry.register(new ResourceLocation(ExtraDetails.MOD_ID, "lever"),
			new ClientBlockType.Builder<LeverClientBlock>(LeverClientBlock::new).validBlocks(Blocks.LEVER).build());
}

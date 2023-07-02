package me.pandamods.extra_details.registries;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.entity.block.DoorEntity;
import me.pandamods.extra_details.entity.block.TrapDoorEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityRegistry {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ExtraDetails.MOD_ID, Registries.BLOCK_ENTITY_TYPE);

	public static RegistrySupplier<BlockEntityType<DoorEntity>> DOOR_ENTITY =
			registerBlockEntity("door", DoorEntity::new, DoorBlock.class);
	public static RegistrySupplier<BlockEntityType<TrapDoorEntity>> TRAP_DOOR_ENTITY =
			registerBlockEntity("trap_door", TrapDoorEntity::new, TrapDoorBlock.class);

	public static <T extends BlockEntity, C extends Block> RegistrySupplier<BlockEntityType<T>> registerBlockEntity(
			String name, BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier, Class<C> instance) {
		List<Block> blocks = new ArrayList<>();
		for (Block block : BuiltInRegistries.BLOCK) {
			if (instance.isInstance(block)) {
				blocks.add(block);
			}
		}

		return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(blockEntitySupplier, blocks.toArray(new Block[0])).build(null));
	}
}

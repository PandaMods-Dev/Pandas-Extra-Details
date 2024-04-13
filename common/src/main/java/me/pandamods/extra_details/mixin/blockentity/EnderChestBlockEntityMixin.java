package me.pandamods.extra_details.mixin.blockentity;

import me.pandamods.extra_details.impl.IChest;
import me.pandamods.extra_details.pandalib.cache.ObjectCache;
import me.pandamods.extra_details.pandalib.entity.MeshAnimatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(EnderChestBlockEntity.class)
public abstract class EnderChestBlockEntityMixin extends BlockEntity implements IChest, MeshAnimatable {
	@Shadow @Final private ChestLidController chestLidController;
	private final ObjectCache cache = new ObjectCache();

	public EnderChestBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	@Override
	public ObjectCache getCache() {
		return cache;
	}

	@Override
	public ChestLidController extraDetails$getChestLidController() {
		return this.chestLidController;
	}
}

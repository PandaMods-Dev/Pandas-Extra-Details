package me.pandamods.extra_details.mixin.blockentity;

import me.pandamods.extra_details.impl.IChest;
import me.pandamods.pandalib.cache.MeshCache;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ChestLidController;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(EnderChestBlockEntity.class)
public class EnderChestBlockEntityMixin implements IChest, MeshAnimatable {
	@Shadow @Final private ChestLidController chestLidController;
	private final MeshCache cache = new MeshCache();
	@Override
	public MeshCache getCache() {
		return cache;
	}

	@Override
	public ChestLidController extraDetails$getChestLidController() {
		return this.chestLidController;
	}
}

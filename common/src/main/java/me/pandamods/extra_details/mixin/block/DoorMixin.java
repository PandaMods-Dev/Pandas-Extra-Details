package me.pandamods.extra_details.mixin.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.registries.BlockEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DoorBlock.class)
public abstract class DoorMixin extends Block implements EntityBlock {
	public DoorMixin(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull RenderShape getRenderShape(BlockState blockState) {
		return ExtraDetails.enable_door_animation ? RenderShape.INVISIBLE : RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return ExtraDetails.enable_door_animation ? BlockEntityRegistry.DOOR_ENTITY.get().create(blockPos, blockState) : null;
	}
}

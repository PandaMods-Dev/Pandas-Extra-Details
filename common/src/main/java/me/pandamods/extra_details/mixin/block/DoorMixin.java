package me.pandamods.extra_details.mixin.block;

import me.pandamods.extra_details.ExtraDetails;
import me.pandamods.extra_details.config.PersistentConfig;
import me.pandamods.extra_details.entity.BetterEntityBlock;
import me.pandamods.extra_details.registries.BlockEntityRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
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
public abstract class DoorMixin extends Block implements BetterEntityBlock {
	public DoorMixin(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context);
	}

	@Override
	public @NotNull RenderShape getRenderShape(BlockState blockState) {
		return PersistentConfig.enable_door_animation ? RenderShape.INVISIBLE : RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		Minecraft.getInstance().level.setBlockAndUpdate(pos, state);
		ExtraDetails.LOGGER.error("trying to create entity");
		return PersistentConfig.enable_door_animation ? BlockEntityRegistry.DOOR_ENTITY.get().create(pos, state) : null;
	}

	@Override
	public Class<?> validBlockClass() {
		return DoorBlock.class;
	}
}

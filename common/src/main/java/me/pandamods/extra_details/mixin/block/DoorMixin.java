package me.pandamods.extra_details.mixin.block;

import com.mojang.blaze3d.vertex.PoseStack;
import me.pandamods.extra_details.config.PersistentConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(DoorBlock.class)
public abstract class DoorMixin extends Block {
	public DoorMixin(Properties properties) {
		super(properties);
	}

	@Override
	public @NotNull RenderShape getRenderShape(BlockState blockState) {
		return PersistentConfig.enable_door_animation ? RenderShape.INVISIBLE : RenderShape.MODEL;
	}

//	@Nullable
//	@Override
//	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
//		return PersistentConfig.enable_door_animation ? BlockEntityRegistry.DOOR_ENTITY.get().create(pos, state) : null;
//	}
//
//	@Override
//	public Class<?> validBlockClass() {
//		return DoorBlock.class;
//	}
}

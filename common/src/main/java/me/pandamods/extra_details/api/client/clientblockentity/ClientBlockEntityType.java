package me.pandamods.extra_details.api.client.clientblockentity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class ClientBlockEntityType<T extends ClientBlockEntity> {
	private final ClientBlockEntitySupplier<? extends T> factory;
	private final Set<Block> validBlocks;
	private final boolean shouldHideBase;

	@Nullable
	public static ResourceLocation getKey(BlockEntityType<?> blockEntityType) {
		return BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntityType);
	}

	public ClientBlockEntityType(ClientBlockEntitySupplier<? extends T> factory, Set<Block> validBlocks, boolean shouldHideBase) {
		this.factory = factory;
		this.validBlocks = validBlocks;
		this.shouldHideBase = shouldHideBase;
	}


	@Nullable
	public T create(BlockPos pos, BlockState state) {
		return this.factory.create(pos, state);
	}

	public boolean isValid(BlockState state) {
		return this.validBlocks.contains(state.getBlock());
	}

	@Nullable
	public T getBlockEntity(BlockGetter level, BlockPos pos) {
		ClientBlockEntity blockEntity = level.getClientBlockEntity(pos);
		return blockEntity != null && blockEntity.getType() == this ? (T) blockEntity : null;
	}

	public boolean shouldHideBase() {
		return shouldHideBase;
	}

	public static final class Builder<T extends ClientBlockEntity> {
		private final ClientBlockEntitySupplier<? extends T> factory;
		final Set<Block> validBlocks;
		private boolean shouldHideBase = false;


		private Builder(ClientBlockEntitySupplier<? extends T> factory, Set<Block> validBlocks) {
			this.factory = factory;
			this.validBlocks = validBlocks;
		}

		public static <T extends ClientBlockEntity> Builder<T> of(ClientBlockEntitySupplier<? extends T> factory,
																						Block... validBlocks) {
			return new Builder<>(factory, ImmutableSet.copyOf(validBlocks));
		}

		public Builder<T> hideBase() {
			this.shouldHideBase = true;
			return this;
		}

		public ClientBlockEntityType<T> build() {
			return new ClientBlockEntityType<>(this.factory, this.validBlocks, this.shouldHideBase);
		}
	}

	@FunctionalInterface
	public interface ClientBlockEntitySupplier<T extends ClientBlockEntity> {
		T create(BlockPos blockPos, BlockState blockState);
	}
}

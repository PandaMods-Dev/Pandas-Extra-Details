package me.pandamods.extra_details.api.clientblockentity;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ClientBlockEntity {
	private final ClientBlockEntityType<?> type;
	private final BlockPos blockPos;
	private BlockState blockState;
	private ClientLevel level;

	public ClientBlockEntity(ClientBlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
		this.type = type;
		this.blockPos = blockPos;
		this.blockState = blockState;
	}

	@Nullable
	public Level getLevel() {
		return this.level;
	}

	public void setLevel(ClientLevel level) {
		this.level = level;
	}

	public boolean hasLevel() {
		return this.level != null;
	}

	public ClientBlockEntityType<?> getType() {
		return type;
	}

	public BlockPos getBlockPos() {
		return blockPos;
	}

	public BlockState getBlockState() {
		return blockState;
	}

	public void setBlockState(BlockState blockState) {
		this.blockState = blockState;
	}
}

package me.pandamods.extra_details.api.client.render.block;

import net.minecraft.CrashReportCategory;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class ClientBlock {
	private final BlockPos blockPos;
	private BlockState blockState;
	@Nullable
	private ClientLevel level;
	private final ClientBlockType<?> type;

	public ClientBlock(ClientBlockType<?> type, BlockPos blockPos, BlockState blockState, @Nullable ClientLevel level) {
		this.blockPos = blockPos;
		this.blockState = blockState;
		this.level = level;
		this.type = type;
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

	@Nullable
	public ClientLevel getLevel() {
		return level;
	}
	public void setLevel(@Nullable ClientLevel level) {
		this.level = level;
	}
	public boolean hasLevel() {
		return level != null;
	}

	public <T extends ClientBlock> ClientBlockType<T> getType() {
		return (ClientBlockType<T>) type;
	}

	public void fillCrashReportCategory(CrashReportCategory reportCategory) {
		reportCategory.setDetail("Name", () -> type.name + "//" + this.getClass().getCanonicalName());
		if (this.level == null) {
			return;
		}
		CrashReportCategory.populateBlockDetails(reportCategory, this.level, this.blockPos, this.getBlockState());
		CrashReportCategory.populateBlockDetails(reportCategory, this.level, this.blockPos, this.level.getBlockState(this.blockPos));
	}
}

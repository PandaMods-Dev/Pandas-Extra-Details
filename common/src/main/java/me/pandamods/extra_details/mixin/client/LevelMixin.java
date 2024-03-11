package me.pandamods.extra_details.mixin.client;

import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.impl.LevelExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Level.class)
public abstract class LevelMixin implements LevelAccessor, AutoCloseable, LevelExtension {
	@Shadow public abstract LevelChunk getChunkAt(BlockPos pos);

	@Override
	public void setClientBlockEntity(ClientBlockEntity clientBlockEntity) {
		this.getChunkAt(clientBlockEntity.getBlockPos()).setClientBlockEntity(clientBlockEntity);
	}

	@Override
	public void removeClientBlockEntity(BlockPos blockPos) {
		this.getChunkAt(blockPos).removeClientBlockEntity(blockPos);
	}

	@Override
	public ClientBlockEntity getClientBlockEntity(BlockPos blockPos) {
		if (this.isOutsideBuildHeight(blockPos)) return null;
		return this.getChunkAt(blockPos).getClientBlockEntity(blockPos);
	}
}

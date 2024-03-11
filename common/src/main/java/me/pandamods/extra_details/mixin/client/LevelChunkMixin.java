package me.pandamods.extra_details.mixin.client;

import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.impl.LevelChunkExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin implements LevelChunkExtension {
	@Shadow @Final private Level level;

	@Unique
	private Map<BlockPos, ClientBlockEntity> clientBlockEntities = new HashMap<>();

	@Override
	public Map<BlockPos, ClientBlockEntity> getClientBlockEntities() {
		return clientBlockEntities;
	}

	@Override
	public void setClientBlockEntity(ClientBlockEntity clientBlockEntity) {
		if (this.level instanceof ClientLevel clientLevel)
			clientBlockEntity.setLevel(clientLevel);
		getClientBlockEntities().put(clientBlockEntity.getBlockPos().immutable(), clientBlockEntity);
	}

	@Override
	public void removeClientBlockEntity(BlockPos blockPos) {
		getClientBlockEntities().remove(blockPos);
	}

	@Override
	public ClientBlockEntity getClientBlockEntity(BlockPos blockPos) {
		return getClientBlockEntities().get(blockPos);
	}
}

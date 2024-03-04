package me.pandamods.extra_details.mixin.client;

import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntityRegistry;
import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntityType;
import me.pandamods.extra_details.api.impl.LevelChunkExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

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

package me.pandamods.extra_details.mixin.client;

import com.google.common.collect.ImmutableMap;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.impl.RenderChunkExtension;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RenderChunk.class)
public class RenderChunkMixin implements RenderChunkExtension {

	private Map<BlockPos, ClientBlockEntity> clientBlockEntities;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void init(LevelChunk wrapped, CallbackInfo ci) {
		this.clientBlockEntities = ImmutableMap.copyOf(wrapped.getClientBlockEntities());
	}

	@Override
	public ClientBlockEntity getClientBlockEntity(BlockPos blockPos) {
		return this.clientBlockEntities.get(blockPos);
	}
}

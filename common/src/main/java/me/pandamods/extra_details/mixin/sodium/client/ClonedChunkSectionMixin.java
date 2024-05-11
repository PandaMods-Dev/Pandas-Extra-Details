package me.pandamods.extra_details.mixin.sodium.client;

import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMaps;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSection;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.extensions.ClonedChunkSectionExtension;
import me.pandamods.extra_details.utils.ExtraDetailsSodiumPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = ClonedChunkSection.class, remap = false)
public class ClonedChunkSectionMixin implements ClonedChunkSectionExtension {
	@Unique
	private Int2ReferenceMap<ClientBlockEntity> clientBlockEntityMap;

	@Override
	public Int2ReferenceMap<ClientBlockEntity> getClientBlockEntityMap() {
		return this.clientBlockEntityMap;
	}

	@Inject(method = "<init>", at = @At("RETURN"))
	public void init(Level world, LevelChunk chunk, LevelChunkSection section, SectionPos pos, CallbackInfo ci) {
		Int2ReferenceMap<ClientBlockEntity> clientBlockEntityMap = null;
		if (section != null && !section.hasOnlyAir()) {
			clientBlockEntityMap = copyClientBlockEntities(chunk, pos);
		}
		this.clientBlockEntityMap = clientBlockEntityMap;
	}

	private static Int2ReferenceMap<ClientBlockEntity> copyClientBlockEntities(LevelChunk chunk, SectionPos chunkCoord) {
		BoundingBox box = new BoundingBox(chunkCoord.minBlockX(), chunkCoord.minBlockY(), chunkCoord.minBlockZ(),
				chunkCoord.maxBlockX(), chunkCoord.maxBlockY(), chunkCoord.maxBlockZ());
		Int2ReferenceOpenHashMap<ClientBlockEntity> clientBlockEntities = null;

		for (Map.Entry<BlockPos, ClientBlockEntity> entry : chunk.getClientBlockEntities().entrySet()) {
			BlockPos pos = entry.getKey();
			ClientBlockEntity entity = entry.getValue();
			if (box.isInside(pos)) {
				if (clientBlockEntities == null) {
					clientBlockEntities = new Int2ReferenceOpenHashMap<>();
				}

				clientBlockEntities.put(ExtraDetailsSodiumPlatform.getLocalBlockIndex(pos.getX() & 15, pos.getY() & 15, pos.getZ() & 15), entity);
			}
		}

		if (clientBlockEntities != null) {
			clientBlockEntities.trim();
		}

		return clientBlockEntities;
	}
}

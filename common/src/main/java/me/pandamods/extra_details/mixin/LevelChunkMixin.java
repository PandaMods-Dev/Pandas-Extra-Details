package me.pandamods.extra_details.mixin;

import me.pandamods.extra_details.api.blockdata.BlockData;
import me.pandamods.extra_details.api.extensions.LevelChunkExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(LevelChunk.class)
public abstract class LevelChunkMixin implements LevelChunkExtension {
	@Unique
	private final Map<BlockPos, BlockData> extraDetails$blockDataMap = new HashMap<>();

	@Override
	public Map<BlockPos, BlockData> extraDetails$getBlockDataMap() {
		return extraDetails$blockDataMap;
	}
}

package me.pandamods.extra_details.mixin;

import me.pandamods.extra_details.api.blockdata.BlockData;
import me.pandamods.extra_details.api.extensions.LevelExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
@Mixin(Level.class)
public abstract class LevelMixin implements LevelExtension {
	@Shadow public abstract LevelChunk getChunkAt(BlockPos pos);

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BlockData> T extraDetails$getBlockData(BlockPos blockPos, BiFunction<BlockPos, Level, T> newDataProvider) {
		LevelChunk chunk = this.getChunkAt(blockPos);
		BlockData blockData = chunk.extraDetails$getBlockDataMap().get(blockPos);

		if (blockData != null && !blockData.validate()) {
			chunk.extraDetails$getBlockDataMap().remove(blockPos);
			blockData = null;
		}

		if (blockData == null) {
			chunk.extraDetails$getBlockDataMap().put(blockPos, blockData = newDataProvider.apply(blockPos, (Level) (Object) this));
		}
		return (T) blockData;
	}
}
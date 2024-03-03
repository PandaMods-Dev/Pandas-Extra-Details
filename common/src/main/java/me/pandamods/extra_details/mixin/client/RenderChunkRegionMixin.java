package me.pandamods.extra_details.mixin.client;

import me.pandamods.extra_details.api.client.clientblockentity.ClientBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Environment(EnvType.CLIENT)
@Mixin(RenderChunkRegion.class)
public abstract class RenderChunkRegionMixin implements BlockAndTintGetter {
	@Shadow @Final protected RenderChunk[][] chunks;

	@Shadow @Final private int centerX;

	@Shadow @Final private int centerZ;

	@Override
	public ClientBlockEntity getClientBlockEntity(BlockPos blockPos) {
		int x = SectionPos.blockToSectionCoord(blockPos.getX()) - this.centerX;
		int y = SectionPos.blockToSectionCoord(blockPos.getZ()) - this.centerZ;
		return this.chunks[x][y].getClientBlockEntity(blockPos);
	}
}

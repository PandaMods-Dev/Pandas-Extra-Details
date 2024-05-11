package me.pandamods.extra_details.mixin.sodium.client;

import com.llamalad7.mixinextras.sugar.Local;
import it.unimi.dsi.fastutil.ints.Int2ReferenceMap;
import me.jellysquid.mods.sodium.client.world.WorldSlice;
import me.jellysquid.mods.sodium.client.world.cloned.ChunkRenderContext;
import me.jellysquid.mods.sodium.client.world.cloned.ClonedChunkSection;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.extensions.ClonedChunkSectionExtension;
import me.pandamods.extra_details.utils.ExtraDetailsSodiumPlatform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = WorldSlice.class, remap = false)
public abstract class WorldSliceMixin implements BlockAndTintGetter {
	@Shadow private int originX;

	@Shadow private int originZ;

	@Shadow @Final private static int SECTION_ARRAY_SIZE;
	@Shadow private BoundingBox volume;
	@Shadow private int originY;

	private Int2ReferenceMap<ClientBlockEntity>[] clientBlockEntityArrays;

	@Inject(method = "<init>", at = @At("RETURN"))
	public void init(ClientLevel world, CallbackInfo ci) {
		this.clientBlockEntityArrays = new Int2ReferenceMap[SECTION_ARRAY_SIZE];
	}

	@Inject(method = "copySectionData", at = @At("RETURN"))
	public void copySectionData(ChunkRenderContext context, int sectionIndex, CallbackInfo ci, @Local ClonedChunkSection section) {
		this.clientBlockEntityArrays[sectionIndex] = ((ClonedChunkSectionExtension) section).getClientBlockEntityMap();
	}

	@Inject(method = "reset", at = @At(
			value = "FIELD",
			target = "Lme/jellysquid/mods/sodium/client/world/WorldSlice;blockEntityArrays:[Lit/unimi/dsi/fastutil/ints/Int2ReferenceMap;"
	))
	public void reset(CallbackInfo ci, @Local int sectionIndex) {
		this.clientBlockEntityArrays[sectionIndex] = null;
	}

	@Override
	public ClientBlockEntity getClientBlockEntity(BlockPos blockPos) {
		if (!this.volume.isInside(blockPos.getX(), blockPos.getY(), blockPos.getZ())) {
			return null;
		} else {
			int relX = blockPos.getX() - this.originX;
			int relY = blockPos.getY() - this.originY;
			int relZ = blockPos.getZ() - this.originZ;
			Int2ReferenceMap<ClientBlockEntity> blockEntities =
					this.clientBlockEntityArrays[ExtraDetailsSodiumPlatform.getLocalSectionIndex(relX >> 4, relY >> 4, relZ >> 4)];
			return blockEntities == null ? null : blockEntities.get(ExtraDetailsSodiumPlatform.getLocalBlockIndex(relX & 15, relY & 15, relZ & 15));
		}
	}
}

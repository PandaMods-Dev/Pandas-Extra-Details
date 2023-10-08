package me.pandamods.extra_details.mixin.pandalib.sodium;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.lists.ChunkRenderList;
import me.jellysquid.mods.sodium.client.util.iterator.ByteArrayIterator;
import me.jellysquid.mods.sodium.client.util.iterator.ByteIterator;
import me.pandamods.pandalib.mixin_extensions.ChunkRenderListExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(value = ChunkRenderList.class, remap = false)
public class ChunkRenderListMixin implements ChunkRenderListExtension {
	private final byte[] sectionsWithClientBlocks = new byte[256];;
	private int sectionsWithClientBlocksCount = 0;

	@Inject(method = "reset", at = @At("HEAD"))
	public void add(int frame, CallbackInfo ci) {
		this.sectionsWithClientBlocksCount = 0;
	}

	@Inject(
			method = "add",
			at = @At(
					value = "FIELD",
					target = "Lme/jellysquid/mods/sodium/client/render/chunk/lists/ChunkRenderList;sectionsWithEntities:[B"
			)
	)
	public void add(RenderSection section, CallbackInfo ci) {
		this.sectionsWithClientBlocks[this.sectionsWithClientBlocksCount] = (byte) section.getSectionIndex();
		this.sectionsWithClientBlocksCount += section.getFlags() >>> 3 & 1;
	}

	@Override
	public ByteIterator extraDetails$sectionsWithClientBlocksIterator() {
		return this.sectionsWithClientBlocksCount == 0 ? null : new ByteArrayIterator(this.sectionsWithClientBlocks, this.sectionsWithClientBlocksCount);
	}

	@Override
	public int extraDetails$getSectionsWithClientBlocksCount() {
		return this.sectionsWithClientBlocksCount;
	}
}

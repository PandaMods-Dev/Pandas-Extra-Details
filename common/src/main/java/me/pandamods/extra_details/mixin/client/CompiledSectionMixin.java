package me.pandamods.extra_details.mixin.client;

import me.pandamods.extra_details.api.extensions.CompiledSectionExtension;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;

@Mixin(SectionRenderDispatcher.CompiledSection.class)
public class CompiledSectionMixin implements CompiledSectionExtension {
	@Unique
	private Set<BlockPos> renderableBlocks = new HashSet<>();

	@Override
	public Set<BlockPos> getRenderableBlocks() {
		return renderableBlocks;
	}
}

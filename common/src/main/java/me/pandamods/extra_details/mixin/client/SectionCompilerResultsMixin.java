package me.pandamods.extra_details.mixin.client;

import me.pandamods.extra_details.api.extensions.SectionCompilerResultsExtension;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;

@Mixin(SectionCompiler.Results.class)
public class SectionCompilerResultsMixin implements SectionCompilerResultsExtension {
	@Unique
	private Set<BlockPos> renderableBlocks = new HashSet<>();

	@Override
	public Set<BlockPos> getRenderableBlocks() {
		return renderableBlocks;
	}
}

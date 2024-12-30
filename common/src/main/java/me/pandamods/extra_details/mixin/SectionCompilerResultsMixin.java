package me.pandamods.extra_details.mixin;

import me.pandamods.extra_details.extensions.SectionCompilerResultsExtension;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.List;

@Mixin(SectionCompiler.Results.class)
	public class SectionCompilerResultsMixin implements SectionCompilerResultsExtension {
		@Unique
		private List<BlockPos> blocks = new ArrayList<>();
		
		@Override
		public List<BlockPos> extraDetails$getRenderableBlocks() {
			return blocks;
		}
	}
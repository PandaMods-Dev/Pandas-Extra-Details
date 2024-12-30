package me.pandamods.extra_details.mixin;

import com.google.common.collect.Lists;
import me.pandamods.extra_details.extensions.CompiledSectionExtension;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(SectionRenderDispatcher.CompiledSection.class)
public class CompiledSectionMixin implements CompiledSectionExtension {
	@Unique
	final List<BlockPos> extraDetails$renderableBlocks = Lists.newArrayList();


	@Override
	public List<BlockPos> extraDetails$getRenderableBlocks() {
		return extraDetails$renderableBlocks;
	}
}
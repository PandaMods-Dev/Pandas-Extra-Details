package me.pandamods.extra_details.mixin.sodium.client;

import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.pandamods.extra_details.api.extensions.SectionCompilerResultsExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = BuiltSectionInfo.class, remap = false)
public class BuiltSectionInfoMixin implements SectionCompilerResultsExtension {
	@Unique
	private Set<BlockPos> renderableBlocks = new HashSet<>();

	@Override
	public Set<BlockPos> getRenderableBlocks() {
		return renderableBlocks;
	}
}
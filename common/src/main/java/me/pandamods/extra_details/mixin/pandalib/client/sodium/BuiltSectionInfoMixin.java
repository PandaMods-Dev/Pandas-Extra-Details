package me.pandamods.extra_details.mixin.pandalib.client.sodium;

import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.impl.CompileResultsExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = BuiltSectionInfo.class, remap = false)
public class BuiltSectionInfoMixin implements CompileResultsExtension {
	@Unique
	private Set<BlockPos> blocks = new HashSet<>();

	@Override
	public Set<BlockPos> getBlocks() {
		return blocks;
	}
}

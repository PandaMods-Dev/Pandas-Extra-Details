package me.pandamods.extra_details.mixin.pandalib.sodium;

import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.mixin_extensions.CompileResultsExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = BuiltSectionInfo.class, remap = false)
public class BuiltSectionInfoMixin implements CompileResultsExtension {
	@Unique
	private List<ClientBlock> blocks = new ArrayList<>();

	@Override
	public List<ClientBlock> getBlocks() {
		return blocks;
	}
}

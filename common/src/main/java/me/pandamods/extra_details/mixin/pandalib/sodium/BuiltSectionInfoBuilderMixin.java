package me.pandamods.extra_details.mixin.pandalib.sodium;

import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.mixin_extensions.CompileResultsExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(value = BuiltSectionInfo.Builder.class, remap = false)
public class BuiltSectionInfoBuilderMixin implements CompileResultsExtension {
	@Unique
	private List<ClientBlock> blocks = new ArrayList<>();

	@Override
	public List<ClientBlock> getBlocks() {
		return blocks;
	}

	@Inject(method = "build", at = @At("RETURN"), cancellable = true)
	public void build(CallbackInfoReturnable<BuiltSectionInfo> cir) {
		BuiltSectionInfo builtSectionInfo = cir.getReturnValue();
		((CompileResultsExtension) builtSectionInfo).getBlocks().addAll(getBlocks());

		cir.setReturnValue(builtSectionInfo);
	}
}

package me.pandamods.extra_details.mixin.pandalib.sodium;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.mixin_extensions.CompileResultsExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = RenderSection.class, remap = false)
public abstract class RenderSectionMixin implements CompileResultsExtension {
	@Shadow private int flags;
	@Unique
	private List<ClientBlock> blocks = new ArrayList<>();

	@Override
	public List<ClientBlock> getBlocks() {
		return blocks;
	}

	@Inject(method = "setRenderState", at = @At("RETURN"))
	public void setRenderState(BuiltSectionInfo info, CallbackInfo ci) {
		this.blocks = ((CompileResultsExtension) info).getBlocks();
		if (!this.blocks.isEmpty()) {
			this.flags |= 3;
		}
	}

	@Inject(method = "clearRenderState", at = @At("RETURN"))
	public void clearRenderState(CallbackInfo ci) {
		this.blocks = null;
	}
}

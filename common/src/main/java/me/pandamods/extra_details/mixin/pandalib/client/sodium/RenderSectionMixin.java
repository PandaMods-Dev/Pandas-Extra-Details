package me.pandamods.extra_details.mixin.pandalib.client.sodium;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.impl.CompileResultsExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = RenderSection.class, remap = false)
public abstract class RenderSectionMixin implements CompileResultsExtension {
	@Shadow private int flags;
	@Unique
	private Set<BlockPos> blocks = new HashSet<>();

	@Override
	public Set<BlockPos> getBlocks() {
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

package me.pandamods.extra_details.mixin.sodium.client;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.pandamods.extra_details.api.extensions.SectionCompilerResultsExtension;
import me.pandamods.extra_details.api.extensions.CompiledSectionExtension;
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

import java.util.HashSet;
import java.util.Set;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = RenderSection.class, remap = false)
public abstract class RenderSectionMixin implements CompiledSectionExtension {
	@Shadow private int flags;

	@Unique
	private Set<BlockPos> renderableBlocks = new HashSet<>();

	@Override
	public Set<BlockPos> getRenderableBlocks() {
		return renderableBlocks;
	}

	@Inject(method = "setRenderState", at = @At("RETURN"))
	public void setRenderState(BuiltSectionInfo info, CallbackInfo ci) {
		this.renderableBlocks = ((SectionCompilerResultsExtension) info).getRenderableBlocks();
		if (!this.getRenderableBlocks().isEmpty())
			this.flags |= 2;
	}

	@Inject(method = "clearRenderState", at = @At("RETURN"))
	public void clearRenderState(CallbackInfo ci) {
		this.getRenderableBlocks().clear();
	}
}
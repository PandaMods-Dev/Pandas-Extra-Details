package me.pandamods.extra_details.mixin.sodium.client;

import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.extensions.CompileResultsExtension;
import me.pandamods.extra_details.api.extensions.CompiledChunkExtension;
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
public abstract class RenderSectionMixin implements CompiledChunkExtension {
	@Shadow private int flags;

	@Unique
	private List<ClientBlockEntity> clientBlockEntities = new ArrayList<>();

	@Override
	public List<ClientBlockEntity> getClientBlockEntities() {
		return clientBlockEntities;
	}

	@Inject(method = "setRenderState", at = @At("RETURN"))
	public void setRenderState(BuiltSectionInfo info, CallbackInfo ci) {
		this.getClientBlockEntities().addAll(((CompileResultsExtension) info).getClientBlockEntities());
		if (!this.getClientBlockEntities().isEmpty()) {
			this.flags |= 3;
		}
	}

	@Inject(method = "clearRenderState", at = @At("RETURN"))
	public void clearRenderState(CallbackInfo ci) {
		this.clientBlockEntities.clear();
	}
}
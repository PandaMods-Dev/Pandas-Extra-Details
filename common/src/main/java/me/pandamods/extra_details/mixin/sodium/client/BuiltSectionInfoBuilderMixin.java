package me.pandamods.extra_details.mixin.sodium.client;

import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import me.pandamods.extra_details.api.clientblockentity.ClientBlockEntity;
import me.pandamods.extra_details.api.extensions.CompileResultsExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = BuiltSectionInfo.Builder.class, remap = false)
public class BuiltSectionInfoBuilderMixin implements CompileResultsExtension {
	@Unique
	private List<ClientBlockEntity> clientBlockEntities = new ArrayList<>();

	@Override
	public List<ClientBlockEntity> getClientBlockEntities() {
		return this.clientBlockEntities;
	}

	@Inject(method = "build", at = @At("RETURN"), cancellable = true)
	public void build(CallbackInfoReturnable<BuiltSectionInfo> cir) {
		BuiltSectionInfo builtSectionInfo = cir.getReturnValue();
		((CompileResultsExtension) builtSectionInfo).getClientBlockEntities().addAll(getClientBlockEntities());

		cir.setReturnValue(builtSectionInfo);
	}
}
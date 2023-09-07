package me.pandamods.extra_details.mixin.pandalib.client.sodium;

import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Environment(EnvType.CLIENT)
@Mixin(SodiumWorldRenderer.class)
public class SodiumWorldRendererMixin {
}

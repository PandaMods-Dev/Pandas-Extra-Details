package me.pandamods.extra_details.mixin.pandalib.sodium;

import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.gen.Accessor;

@Pseudo
@Environment(EnvType.CLIENT)
@Mixin(value = SodiumWorldRenderer.class, remap = false)
public interface SodiumWorldRendererAccessor {
	@Accessor
	RenderSectionManager getRenderSectionManager();
}

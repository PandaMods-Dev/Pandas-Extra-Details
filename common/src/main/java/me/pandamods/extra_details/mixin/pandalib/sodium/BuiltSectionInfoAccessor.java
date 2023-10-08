package me.pandamods.extra_details.mixin.pandalib.sodium;

import me.jellysquid.mods.sodium.client.render.chunk.data.BuiltSectionInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(value = BuiltSectionInfo.class, remap = false)
public interface BuiltSectionInfoAccessor {
	@Accessor()
	public void setFlags(int flags);

	@Accessor()
	public int getFlags();
}

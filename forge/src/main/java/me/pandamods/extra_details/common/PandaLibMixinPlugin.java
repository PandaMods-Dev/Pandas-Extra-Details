package me.pandamods.extra_details.common;

import me.pandamods.extra_details.ExtraDetailsMixinPluginCommon;
import net.minecraftforge.fml.loading.LoadingModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class PandaLibMixinPlugin implements IMixinConfigPlugin {
	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return ExtraDetailsMixinPluginCommon.getConditions(s -> LoadingModList.get().getModFileById(s) != null)
				.getOrDefault(mixinClassName, () -> true).get();
	}

	@Override
	public void onLoad(String mixinPackage) {

	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

	}
}

package me.pandamods.extra_details;

import me.pandamods.extra_details.compat.ExtraDetailsCompat;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class ExtraDetailsMixinPlugin implements IMixinConfigPlugin {
	@Override
	public void onLoad(String mixinPackage) {

	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		return switch (mixinClassName) {
			case "me.pandamods.extra_details.mixin.client.LevelRendererMixin" -> !ExtraDetailsCompat.isSodiumLoaded();
			case "me.pandamods.extra_details.mixin.sodium.client.BuiltSectionInfoBuilderMixin",
				 "me.pandamods.extra_details.mixin.sodium.client.SodiumWorldRendererAccessor",
				 "me.pandamods.extra_details.mixin.sodium.client.BuiltSectionInfoMixin",
				 "me.pandamods.extra_details.mixin.sodium.client.ChunkBuilderMeshingTaskMixin",
				 "me.pandamods.extra_details.mixin.sodium.client.ClonedChunkSectionMixin",
				 "me.pandamods.extra_details.mixin.sodium.client.RenderSectionMixin",
				 "me.pandamods.extra_details.mixin.sodium.client.SodiumLevelRendererMixin",
				 "me.pandamods.extra_details.mixin.sodium.client.WorldSliceMixin" -> ExtraDetailsCompat.isSodiumLoaded();
			default -> true;
		};
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

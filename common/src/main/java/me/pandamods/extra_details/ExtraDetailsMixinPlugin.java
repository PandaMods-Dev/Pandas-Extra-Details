/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.extra_details;

import dev.architectury.platform.Platform;
import me.pandamods.pandalib.utils.ClassUtils;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
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
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		List<String> mixins = new ArrayList<>();

		boolean isSodiumLoaded = ClassUtils.doesClassExist("me.jellysquid.mods.sodium.client.SodiumClientMod");
		boolean isNvidiumLoaded = ClassUtils.doesClassExist("me.cortex.nvidium.Nvidium");

		boolean isMinecraftForge = Platform.isMinecraftForge();
		boolean isNeoForge = Platform.isNeoForge();
		boolean isForgeLike = isMinecraftForge || isNeoForge;

		// Mixins for vanilla rendering
		mixins.add("LevelChunkMixin");
		mixins.add("LevelMixin");
		mixins.add("client.CompiledSectionMixin");
		mixins.add("client.RenderSectionRebuildTaskMixin");
		mixins.add("client.SectionCompilerResultsMixin");

		if (!isSodiumLoaded) {
			// Mixins for vanilla rendering if Sodium is not loaded
			mixins.add("client.LevelRendererMixin");
		} else {
			// Mixins for Sodium rendering
			mixins.add("sodium.client.BuiltSectionInfoBuilderMixin");
			mixins.add("sodium.client.SodiumWorldRendererAccessor");
			mixins.add("sodium.client.BuiltSectionInfoMixin");
			mixins.add("sodium.client.ChunkBuilderMeshingTaskMixin");
			mixins.add("sodium.client.RenderSectionMixin");
			if (isNvidiumLoaded) {
				// Mixin for Nvidium rendering
				mixins.add("nvidium.client.LevelRendererMixin");
			} else {
				// Mixin for Sodium rendering if Nvidium is not loaded
				mixins.add("sodium.client.LevelRendererMixin");
			}
		}

		return mixins;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
}

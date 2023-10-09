package me.pandamods.pandalib;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Supplier;

public class PandaLibMixinPluginCommon {
	public static Map<String, Supplier<Boolean>> getConditions(boolean condition) {
		return ImmutableMap.of(
            "me.pandamods.extra_details.mixin.pandalib.sodium.BuiltSectionInfoAccessor", () -> condition,
            "me.pandamods.extra_details.mixin.pandalib.sodium.BuiltSectionInfoBuilderMixin", () -> condition,
            "me.pandamods.extra_details.mixin.pandalib.sodium.BuiltSectionInfoMixin", () -> condition,
            "me.pandamods.extra_details.mixin.pandalib.sodium.ChunkBuilderMeshingTaskMixin", () -> condition,
            "me.pandamods.extra_details.mixin.pandalib.sodium.ChunkRenderListMixin", () -> condition,
            "me.pandamods.extra_details.mixin.pandalib.sodium.RenderSectionMixin", () -> condition,
            "me.pandamods.extra_details.mixin.pandalib.sodium.SodiumWorldRendererAccessor", () -> condition,
            "me.pandamods.extra_details.mixin.pandalib.client.LevelRendererMixin", () -> !condition,
            "me.pandamods.extra_details.mixin.pandalib.sodium.SodiumLevelRendererMixin", () -> condition
		);
	}
}

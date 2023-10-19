package me.pandamods.pandalib;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class PandaLibMixinPluginCommon {
	private static final String sodiumID = "sodium";

	public static Map<String, Supplier<Boolean>> getConditions(Function<String, Boolean> condition) {
		return ImmutableMap.of(
            "me.pandamods.extra_details.mixin.pandalib.sodium.BuiltSectionInfoAccessor", () -> condition.apply(sodiumID),
            "me.pandamods.extra_details.mixin.pandalib.sodium.BuiltSectionInfoBuilderMixin", () -> condition.apply(sodiumID),
            "me.pandamods.extra_details.mixin.pandalib.sodium.BuiltSectionInfoMixin", () -> condition.apply(sodiumID),
            "me.pandamods.extra_details.mixin.pandalib.sodium.ChunkBuilderMeshingTaskMixin", () -> condition.apply(sodiumID),
            "me.pandamods.extra_details.mixin.pandalib.sodium.ChunkRenderListMixin", () -> condition.apply(sodiumID),
            "me.pandamods.extra_details.mixin.pandalib.sodium.RenderSectionMixin", () -> condition.apply(sodiumID),
            "me.pandamods.extra_details.mixin.pandalib.sodium.SodiumWorldRendererAccessor", () -> condition.apply(sodiumID),
            "me.pandamods.extra_details.mixin.pandalib.client.LevelRendererMixin", () -> !condition.apply(sodiumID),
            "me.pandamods.extra_details.mixin.pandalib.sodium.SodiumLevelRendererMixin", () -> condition.apply(sodiumID)
		);
	}
}

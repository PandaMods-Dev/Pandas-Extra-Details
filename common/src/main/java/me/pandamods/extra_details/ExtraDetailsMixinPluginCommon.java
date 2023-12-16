package me.pandamods.extra_details;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ExtraDetailsMixinPluginCommon {
	private static final String sodiumID = "sodium";
	private static final String embeddiumID = "embeddium";

	public static Map<String, Supplier<Boolean>> getConditions(Function<String, Boolean> condition) {
		return ImmutableMap.of(
            "me.pandamods.extra_details.mixin.pandalib.client.sodium.BuiltSectionInfoAccessor", () -> condition.apply(sodiumID) || condition.apply(embeddiumID),
            "me.pandamods.extra_details.mixin.pandalib.client.sodium.BuiltSectionInfoBuilderMixin", () -> condition.apply(sodiumID) || condition.apply(embeddiumID),
            "me.pandamods.extra_details.mixin.pandalib.client.sodium.BuiltSectionInfoMixin", () -> condition.apply(sodiumID) || condition.apply(embeddiumID),
            "me.pandamods.extra_details.mixin.pandalib.client.sodium.ChunkBuilderMeshingTaskMixin", () -> condition.apply(sodiumID) || condition.apply(embeddiumID),
            "me.pandamods.extra_details.mixin.pandalib.client.sodium.ChunkRenderListMixin", () -> condition.apply(sodiumID) || condition.apply(embeddiumID),
            "me.pandamods.extra_details.mixin.pandalib.client.sodium.RenderSectionMixin", () -> condition.apply(sodiumID) || condition.apply(embeddiumID),
            "me.pandamods.extra_details.mixin.pandalib.client.sodium.SodiumWorldRendererAccessor", () -> condition.apply(sodiumID) || condition.apply(embeddiumID),
            "me.pandamods.extra_details.mixin.pandalib.client.LevelRendererMixin", () -> !condition.apply(sodiumID) && !condition.apply(embeddiumID),
            "me.pandamods.extra_details.mixin.pandalib.client.sodium.SodiumLevelRendererMixin", () -> condition.apply(sodiumID) || condition.apply(embeddiumID)
		);
	}
}

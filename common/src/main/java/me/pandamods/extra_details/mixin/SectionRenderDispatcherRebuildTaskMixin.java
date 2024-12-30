package me.pandamods.extra_details.mixin;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.sugar.Local;
import me.pandamods.extra_details.extensions.CompiledSectionExtension;
import me.pandamods.extra_details.extensions.SectionCompilerResultsExtension;
import net.minecraft.client.renderer.chunk.SectionCompiler;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(SectionRenderDispatcher.RenderSection.RebuildTask.class)
public class SectionRenderDispatcherRebuildTaskMixin {
	@Inject(method = "doTask", at = @At(
			value = "FIELD",
			target = "Lnet/minecraft/client/renderer/chunk/SectionRenderDispatcher$CompiledSection;renderableBlockEntities:Ljava/util/List;"
	))
	public void doTask(CallbackInfoReturnable<Boolean> cir,
					   @Local SectionRenderDispatcher.CompiledSection compiledSection,
					   @Local SectionCompiler.Results results) {
		CompiledSectionExtension compiledSectionExtension = (CompiledSectionExtension) compiledSection;
		SectionCompilerResultsExtension resultsExtension = (SectionCompilerResultsExtension) (Object) results;

		assert compiledSectionExtension != null;
		assert resultsExtension != null;

		compiledSectionExtension.extraDetails$getRenderableBlocks().addAll(resultsExtension.extraDetails$getRenderableBlocks());
	}
}

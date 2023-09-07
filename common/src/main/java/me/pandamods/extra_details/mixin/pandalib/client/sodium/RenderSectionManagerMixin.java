package me.pandamods.extra_details.mixin.pandalib.client.sodium;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import me.jellysquid.mods.sodium.client.render.chunk.ChunkRenderMatrices;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSection;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.chunk.passes.BlockRenderPass;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.client.render.block.ClientBlockProvider;
import me.pandamods.pandalib.client.render.block.ClientBlockRegistry;
import me.pandamods.pandalib.mixin_extensions.CompileResultsExtension;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(RenderSectionManager.class)
public abstract class RenderSectionManagerMixin implements CompileResultsExtension {
	@Unique
    private final ObjectList<RenderSection> renderChunks = new ObjectArrayList<>();

    @Inject(method = "addChunkToVisible", at = @At("HEAD"), remap = false)
    public void addChunkToVisible(RenderSection render, CallbackInfo ci) {
        this.renderChunks.add(render);
    }

	@Inject(method = "resetLists", at = @At("TAIL"), remap = false)
    public void resetLists(CallbackInfo ci) {
        this.renderChunks.clear();
    }

	@Override
	public List<ClientBlock> getBlocks() {
		return this.renderChunks.stream().flatMap(container ->
				((CompileResultsExtension) container.getData()).getBlocks().stream()).toList();
	}
}

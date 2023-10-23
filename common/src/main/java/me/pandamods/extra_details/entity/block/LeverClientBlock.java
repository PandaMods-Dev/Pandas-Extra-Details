package me.pandamods.extra_details.entity.block;

import me.pandamods.extra_details.client.animation_controller.block.LeverAnimationController;
import me.pandamods.pandalib.cache.MeshCache;
import me.pandamods.pandalib.client.animation_controller.AnimationController;
import me.pandamods.pandalib.client.animation_controller.AnimationControllerProvider;
import me.pandamods.pandalib.client.render.block.ClientBlock;
import me.pandamods.pandalib.entity.MeshAnimatable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;

public class LeverClientBlock extends ClientBlock implements MeshAnimatable {
	private final MeshCache cache = new MeshCache();

	public float animTime;

	public LeverClientBlock(BlockPos blockPos, BlockState blockState, ClientLevel level) {
		super(blockPos, blockState, level);
		animTime = blockState.getValue(LeverBlock.POWERED) ? 1 : 0;
	}

	@Override
	public MeshCache getCache() {
		return this.cache;
	}
}

package me.pandamods.pandalib.client.animation.states;

import me.pandamods.pandalib.client.animation.AnimatableInstance;
import me.pandamods.pandalib.resource.Animation;
import me.pandamods.pandalib.resource.AssimpResources;
import me.pandamods.pandalib.resource.Mesh;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class AnimationState extends State {
	private final Animation animation;
	private final boolean loop;

	public AnimationState(ResourceLocation resourceLocation) {
		this(resourceLocation, false);
	}

	public AnimationState(ResourceLocation resourceLocation, boolean loop) {
		this(AssimpResources.getAnimation(resourceLocation), loop);
	}

	public AnimationState(Animation animation, boolean loop) {
		this.animation = animation;
		this.loop = loop;
	}

	@Override
	public Matrix4f getBoneMatrix(Mesh.Bone bone) {
		Animation.Channel channel = this.animation.getChannel(bone.getName());
		if (channel != null) {
			return channel.getMatrix(this.getTime());
		}
		return new Matrix4f(bone.getOffsetMatrix());
	}

	@Override
	public void updateTime(AnimatableInstance instance, float partialTick) {
		super.updateTime(instance, partialTick);

		if (loop && this.isFinished()) {
			this.start(this.getStartTick() + this.getDuration());
			this.updateTime(instance, partialTick);
		}
	}

	@Override
	public float getDuration() {
		return this.animation.getDuration();
	}
}

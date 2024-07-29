package me.pandamods.pandalib.client.animation.states;

import me.pandamods.pandalib.client.animation.AnimatableInstance;
import me.pandamods.pandalib.resource.animation.Animation;
import me.pandamods.pandalib.resource.AssimpResources;
import me.pandamods.pandalib.resource.animation.Channel;
import me.pandamods.pandalib.resource.model.Node;
import me.pandamods.pandalib.utils.PrintUtils;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

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
	public Matrix4f getBoneTransform(Node node) {
		Channel channel = this.animation.getChannel(node.getName());
		if (channel != null) {
			return channel.getMatrix(this.getTime());
		}
		return new Matrix4f(node.getInitialTransform());
	}

	@Override
	public void updateTime(AnimatableInstance instance, float partialTick) {
		super.updateTime(instance, partialTick);

		if (loop && this.isFinished()) {
			this.start(this.getStartTick() + (this.getDuration() * 20));
			this.updateTime(instance, partialTick);
		}
	}

	@Override
	public float getDuration() {
		return this.animation.getDuration();
	}
}

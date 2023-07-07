package me.pandamods.extra_details.utils.animation;

import software.bernie.geckolib.core.animation.RawAnimation;

public class Animations {
	public static final RawAnimation OPEN = RawAnimation.begin().thenPlayAndHold("action.open");
	public static final RawAnimation CLOSE = RawAnimation.begin().thenPlayAndHold("action.close");
	public static final RawAnimation ON = RawAnimation.begin().thenPlayAndHold("action.on");
	public static final RawAnimation OFF = RawAnimation.begin().thenPlayAndHold("action.off");
}

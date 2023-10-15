package me.pandamods.pandalib.utils.time_testing;

public abstract class TimeTesting {
	protected final String actionName;
	protected final long startTime;

	protected TimeTesting(String actionName, long startTime) {
		this.actionName = actionName;
		this.startTime = startTime;
	}

	public static TimeTesting start(String actionName, boolean isNanoSec) {
		if (isNanoSec)
			return new NanoTimeTesting(actionName);
		return new MilliTimeTesting(actionName);
	}

	public abstract void end();
}

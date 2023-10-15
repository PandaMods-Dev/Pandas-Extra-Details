package me.pandamods.pandalib.utils.time_testing;

import me.pandamods.extra_details.ExtraDetails;

import java.text.MessageFormat;

public class NanoTimeTesting extends TimeTesting {
	public NanoTimeTesting(String actionName) {
		super(actionName, System.nanoTime());
	}

	@Override
	public void end() {
		long endTime = System.nanoTime();
		long elapsedTime = endTime - this.startTime;

//		ExtraDetails.LOGGER.info(MessageFormat.format("[{0}] Elapsed time in nanoseconds: " + elapsedTime, actionName));
		System.out.println(MessageFormat.format("[{0}] Elapsed time in nanoseconds: " + elapsedTime, actionName));
	}
}

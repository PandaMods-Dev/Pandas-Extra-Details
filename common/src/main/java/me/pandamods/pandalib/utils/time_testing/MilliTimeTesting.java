package me.pandamods.pandalib.utils.time_testing;

import me.pandamods.extra_details.ExtraDetails;

import java.text.MessageFormat;

public class MilliTimeTesting extends TimeTesting {
	public MilliTimeTesting(String actionName) {
		super(actionName, System.currentTimeMillis());
	}

	@Override
	public void end() {
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - this.startTime;

//		ExtraDetails.LOGGER.info(MessageFormat.format("[{0}] Elapsed time in milliseconds: " + elapsedTime, actionName));
		System.out.println(MessageFormat.format("[{0}] Elapsed time in milliseconds: " + elapsedTime, actionName));
	}
}

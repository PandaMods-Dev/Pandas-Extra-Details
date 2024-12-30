package me.pandamods.extra_details.client;

import me.pandamods.extra_details.api.render.ExtraDetailsBlockRenderDispatcher;

public class ExtraDetailsClient {
	private static ExtraDetailsClient INSTANCE;
	
	public final ExtraDetailsBlockRenderDispatcher blockRenderDispatcher = new ExtraDetailsBlockRenderDispatcher();
	
	public ExtraDetailsClient() {
		INSTANCE = this;
	}

	public static ExtraDetailsClient getInstance() {
		return INSTANCE;
	}
}

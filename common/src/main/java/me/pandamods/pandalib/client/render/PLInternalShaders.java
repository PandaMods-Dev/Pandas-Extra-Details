/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class PLInternalShaders {
	private static ShaderInstance renderTypeMesh;

	public static void setRenderTypeMesh(ShaderInstance instance) {
		renderTypeMesh = instance;
	}

	@Nullable
	public static ShaderInstance getRenderTypeMesh() {
		return renderTypeMesh;
	}

	public static void register(ResourceProvider resourceProvider, ClientReloadShadersEvent.ShadersSink shadersSink) {
		try {
			shadersSink.registerShader(new ShaderInstance(resourceProvider, "pandalib/mesh",
					DefaultVertexFormat.NEW_ENTITY), PLInternalShaders::setRenderTypeMesh);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

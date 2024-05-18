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

package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import me.pandamods.extra_details.ExtraDetails;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class PLInternalShaders {
	private static ShaderInstance renderTypeTriangularShader;

	public static void setRenderTypeTriangularShader(ShaderInstance instance) {
		renderTypeTriangularShader = instance;
	}

	@Nullable
	public static ShaderInstance getRenderTypeTriangularShader() {
		return renderTypeTriangularShader;
	}

	public static void register(ResourceProvider resourceProvider, ClientReloadShadersEvent.ShadersSink shadersSink) {
		try {
			shadersSink.registerShader(new ShaderInstance(resourceProvider, "pandalib/rendertype_triangular",
					DefaultVertexFormat.NEW_ENTITY), PLInternalShaders::setRenderTypeTriangularShader);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

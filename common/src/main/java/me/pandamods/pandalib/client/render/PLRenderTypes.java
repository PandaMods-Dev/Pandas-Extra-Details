package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class PLRenderTypes extends RenderType {
	public static final RenderStateShard.ShaderStateShard RENDERTYPE_TRIANGULAR_SHADER =
			new RenderStateShard.ShaderStateShard(PLInternalShaders::getRenderTypeTriangularShader);

	public static final RenderType RENDERTYPE_TRIANGULAR = RenderType.create("triangular", DefaultVertexFormat.NEW_ENTITY,
			VertexFormat.Mode.TRIANGLES, 256, true, false,
			RenderType.CompositeState.builder().setLightmapState(RenderType.LIGHTMAP)
					.setShaderState(RENDERTYPE_TRIANGULAR_SHADER).createCompositeState(true)
	);

	public PLRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling,
						 boolean sortOnUpload, Runnable setupState, Runnable clearState) {
		super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
	}
}

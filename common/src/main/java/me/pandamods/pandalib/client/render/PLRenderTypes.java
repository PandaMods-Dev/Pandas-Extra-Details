package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

public class PLRenderTypes extends RenderType {
	private static final ShaderStateShard TRIANGULAR_SHADER =
			new ShaderStateShard(PLInternalShaders::getRenderTypeTriangularShader);


	private static final RenderType SOLID_TRIANGULAR = RenderType.create("solid_triangular",
			DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 0x200000, true, false,
			CompositeState.builder()
					.setLightmapState(LIGHTMAP)
					.setShaderState(TRIANGULAR_SHADER)
					.setTextureState(BLOCK_SHEET_MIPPED)
					.createCompositeState(true)
	);

	public static RenderType solidTriangular() {
		return SOLID_TRIANGULAR;
	}

	private static final RenderType CUTOUT_TRIANGULAR = RenderType.create("cutout_triangular",
			DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 131072, true, false,
			CompositeState.builder()
					.setLightmapState(LIGHTMAP)
					.setShaderState(TRIANGULAR_SHADER)
					.setTextureState(BLOCK_SHEET)
					.createCompositeState(true)
	);

	public static RenderType cutoutTriangular() {
		return CUTOUT_TRIANGULAR;
	}

	private static final RenderType MESH = RenderType.create("mesh",
			DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 131072, true, false,
			CompositeState.builder()
					.setLightmapState(LIGHTMAP)
					.setShaderState(TRIANGULAR_SHADER)
					.setTextureState(BLOCK_SHEET)
					.createCompositeState(true)
	);

	public static RenderType mesh() {
		return CUTOUT_TRIANGULAR;
	}

	public PLRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling,
						 boolean sortOnUpload, Runnable setupState, Runnable clearState) {
		super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
	}
}

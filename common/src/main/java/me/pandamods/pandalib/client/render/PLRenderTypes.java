package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

public class PLRenderTypes extends RenderType {
	private static final ShaderStateShard MESH_SHADER =
			new ShaderStateShard(PLInternalShaders::getRenderTypeMesh);


	private static final RenderType CUTOUT_MESH = RenderType.create("cutout_mesh",
			DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 131072, true, false,
			CompositeState.builder()
					.setLightmapState(LIGHTMAP)
					.setShaderState(MESH_SHADER)
					.setTextureState(BLOCK_SHEET)
					.createCompositeState(true)
	);

	public static RenderType cutoutMesh() {
		return CUTOUT_MESH;
	}

	public PLRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling,
						 boolean sortOnUpload, Runnable setupState, Runnable clearState) {
		super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
	}
}

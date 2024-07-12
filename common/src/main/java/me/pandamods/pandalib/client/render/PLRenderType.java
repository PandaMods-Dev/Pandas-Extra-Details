package me.pandamods.pandalib.client.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class PLRenderType extends RenderType {
	private static final ShaderStateShard MESH_SHADER =
			new ShaderStateShard(PLInternalShaders::getRenderTypeMesh);


	public static final RenderType CUTOUT_MESH = create("cutout_mesh",
			DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 131072, true, false,
			CompositeState.builder()
					.setLightmapState(LIGHTMAP)
					.setShaderState(MESH_SHADER)
					.setTextureState(BLOCK_SHEET)
					.createCompositeState(true)
	);

	public static final Function<ResourceLocation, RenderType> CUTOUT_MESH_ENTITY = Util.memoize((resourceLocation) -> {
		CompositeState compositeState = CompositeState.builder()
				.setShaderState(RENDERTYPE_ENTITY_CUTOUT_SHADER)
				.setTextureState(new TextureStateShard(resourceLocation, false, false))
				.setTransparencyState(NO_TRANSPARENCY).setLightmapState(LIGHTMAP).setOverlayState(OVERLAY)
				.createCompositeState(true);
		return create("cutout_mesh_entity", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLES, 1536,
				true, false, compositeState);
	});

	public PLRenderType(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling,
						boolean sortOnUpload, Runnable setupState, Runnable clearState) {
		super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
	}
}

package me.pandamods.pandalib.resources;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.architectury.event.events.client.ClientReloadShadersEvent;
import me.pandamods.pandalib.utils.gsonadapter.Vector3fTypeAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.joml.Vector3f;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Resources {
	public static final Gson GSON = new GsonBuilder()
			.registerTypeAdapter(Vector3f.class, new Vector3fTypeAdapter())
			.create();

	public static final Map<ResourceLocation, Mesh> meshes = new HashMap<>();

	public static void reloadShaders(ResourceProvider resourceProvider, ClientReloadShadersEvent.ShadersSink shadersSink) {
		meshes.clear();

		ResourceManager manager = Minecraft.getInstance().getResourceManager();
		manager.listResources("meshes", resourceLocation -> resourceLocation.getPath().endsWith(".json")).forEach((resourceLocation, resource) -> {
			try	(InputStream inputStream = resource.open()) {
				meshes.put(resourceLocation, GSON.fromJson(new String(inputStream.readAllBytes()), Mesh.class));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}
}

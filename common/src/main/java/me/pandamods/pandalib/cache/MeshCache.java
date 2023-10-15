package me.pandamods.pandalib.cache;

import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.resources.Mesh;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class MeshCache {
	public Mesh mesh;
	public ResourceLocation meshLocation;
	public Armature armature;
	public Map<Integer, Map<String, vertexVectors>> vertices = new HashMap<>();

	public record vertexVectors(Vector3f position, Vector3f normal) {}
}

package me.pandamods.pandalib.resource;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.joml.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record MeshData(String format_version, List<Vertex> vertices, List<Face> faces) {
	public record Vertex(Vector3fc position, List<Weight> weights, float max_weight) { }
	public record Weight(String name, float weight) { }
	public record Face(Vector3fc normal, List<FaceVertex> vertices, String texture_name) { }
	public record FaceVertex(int index, Vector2fc uv) { }
}
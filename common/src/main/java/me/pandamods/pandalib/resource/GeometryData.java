package me.pandamods.pandalib.resource;

import java.util.List;
import java.util.Map;

public record GeometryData(String formatVersion, List<Geometry> minecraftGeometry) {

	public record Geometry(Description description, List<Bone> bones) {}

	public record Description(String identifier, int textureWidth, int textureHeight,
							  double visibleBoundsWidth, double visibleBoundsHeight,
							  double[] visibleBoundsOffset) {}

	public record Bone(String name, double[] pivot, List<Cube> cubes) {}

	public record Cube(double[] origin, double[] size, Map<String, UV> uv) {}

	public record UV(int[] uv, int[] uvSize) {}

}
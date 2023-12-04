package me.pandamods.pandalib.cache;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.client.animation_controller.AnimationController;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.entity.MeshAnimatable;
import me.pandamods.pandalib.resources.Mesh;
import me.pandamods.pandalib.resources.Resources;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;

public class MeshCache {
	public Mesh mesh;
	public ResourceLocation meshLocation;
	public Armature armature;
	public Map<Integer, Map<String, vertexVectors>> vertices = new HashMap<>();

	public AnimationController<?> animationController;

	public <T extends MeshAnimatable> void updateMeshCache(ResourceLocation location, MeshModel<T> model, T base) {
		if (base.getCache().mesh == null || !base.getCache().meshLocation.equals(location)) {
			base.getCache().meshLocation = location;
			base.getCache().mesh = Resources.MESHES.getOrDefault(location, null);

			if (base.getCache().mesh != null) {
				base.getCache().armature = new Armature(base.getCache().mesh);
				model.setPropertiesOnCreation(base, base.getCache().armature);
			} else {
				PandaLib.LOGGER.error("Can't find mesh at " + location.toString());
			}
		}
	}

	public record vertexVectors(Vector3f position, Vector3f normal) {}
}

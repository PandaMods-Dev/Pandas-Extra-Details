package me.pandamods.pandalib.cache;

import me.pandamods.pandalib.PandaLib;
import me.pandamods.pandalib.client.animation_controller.AnimationController;
import me.pandamods.pandalib.client.model.Armature;
import me.pandamods.pandalib.client.model.MeshModel;
import me.pandamods.pandalib.entity.MeshAnimatable;
import me.pandamods.pandalib.resources.ArmatureRecord;
import me.pandamods.pandalib.resources.MeshRecord;
import me.pandamods.pandalib.resources.Resources;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ObjectCache {
	@Nullable
	public MeshRecord meshRecord;
	@Nullable
	public ResourceLocation meshLocation;

	@Nullable
	public ArmatureRecord armatureRecord;
	@Nullable
	public ResourceLocation armatureLocation;

	@Nullable
	public Armature armature;
	@Nullable
	public AnimationController<?> animationController;

	public Map<String, Map<Integer, VertexCache>> vertices = new HashMap<>();
	public Map<String, Map<Integer, FaceCache>> faces = new HashMap<>();

	public <T extends MeshAnimatable> void updateMeshCache(MeshModel<T> model, T base) {
		ResourceLocation meshLocation = model.getMeshLocation(base);
		if (this.meshRecord == null || !Objects.equals(this.meshLocation, meshLocation)) {
			this.meshLocation = meshLocation;
			this.meshRecord = Resources.MESHES.getOrDefault(meshLocation, null);

			if (this.meshRecord == null && armatureLocation != null) {
				PandaLib.LOGGER.error("Can't find mesh at " + meshLocation);
			}
		}

		ResourceLocation armatureLocation = model.getArmatureLocation(base);
		if (this.armatureRecord == null || !Objects.equals(this.armatureLocation, armatureLocation)) {
			this.armatureLocation = armatureLocation;
			this.armatureRecord = Resources.ARMATURES.getOrDefault(armatureLocation, null);

			if (this.armatureRecord != null) {
				this.armature = new Armature(this.armatureRecord);
				model.setPropertiesOnCreation(base, this.armature);
			} else if (armatureLocation != null) {
				PandaLib.LOGGER.error("Can't find armature at " + armatureLocation);
			}
		}
	}

	public boolean hasMesh() {
		return meshRecord != null;
	}

	public boolean hasArmatureRecord() {
		return armatureRecord != null;
	}

	public boolean hasArmature() {
		return armature != null;
	}

	public record VertexCache(Vector3f position) {}
	public record FaceCache(Vector3f normal) {}
}

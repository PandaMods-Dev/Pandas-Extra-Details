package me.pandamods.extra_details.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Math;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class VectorUtils {
	public static void rotateByPivot(Vector3f vector, Vector3f pivot, Vector3f rotation) {
		pivot = pivot.div(16, new Vector3f());

		vector.add(pivot.x, pivot.y, pivot.z);
		vector.rotate(new Quaternionf().identity().rotateZYX(rotation.z, rotation.y, rotation.x));
		vector.add(-pivot.x, -pivot.y, -pivot.z);
	}

	public static void rotateByPivot(PoseStack poseStack, Vector3f pivot, Vector3f rotation) {
		pivot = pivot.div(16, new Vector3f());

		poseStack.translate(pivot.x, pivot.y, pivot.z);
		poseStack.mulPose(new Quaternionf().identity().rotateZYX(rotation.z, rotation.y, rotation.x));
		poseStack.translate(-pivot.x, -pivot.y, -pivot.z);
	}

	public static Vector3f toRadians(Vector3f vector) {
		return new Vector3f(Math.toRadians(vector.x), Math.toRadians(vector.y), Math.toRadians(vector.z));
	}

	public static Vector3f toDegrees(Vector3f vector) {
		return new Vector3f((float) Math.toDegrees(vector.x), (float) Math.toDegrees(vector.y), (float) Math.toDegrees(vector.z));
	}
}

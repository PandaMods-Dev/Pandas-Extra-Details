package me.pandamods.pandalib.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Math;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.text.DecimalFormat;

public class VectorUtils {
	public static Vector3f rotateVector(Vector3f target, Vector3f rotation) {
		return target.rotateZ(rotation.z).rotateY(rotation.y).rotateX(rotation.x);
	}

	public static PoseStack rotateVector(PoseStack stack, Vector3f rotation) {
		stack.mulPose(new Quaternionf().identity().rotateZYX(rotation.z, rotation.y, rotation.x));
		return stack;
	}

	public static Vector3f rotateByPivot(Vector3f target, Vector3f pivot, Vector3f rotation) {
		target.add(pivot);
		rotateVector(target, rotation);
		return target.sub(pivot);
	}

	public static PoseStack rotateByPivot(PoseStack stack, Vector3f pivot, Vector3f rotation) {
		stack.translate(pivot.x, pivot.y, pivot.z);
		rotateVector(stack, rotation);
		stack.translate(-pivot.x, -pivot.y, -pivot.z);
		return stack;
	}

	public static String betterPrint(Vector3f vector) {
		DecimalFormat decimalFormat = new DecimalFormat("#.###");
		return "(" + decimalFormat.format(vector.x) + " " + decimalFormat.format(vector.y) + " " + decimalFormat.format(vector.z) + ")";
	}

	public static Vector3f toRadians(Vector3f vector) {
		vector = new Vector3f(vector);
		vector.x = Math.toRadians(vector.x);
		vector.y = Math.toRadians(vector.y);
		vector.z = Math.toRadians(vector.z);
		return vector;
	}

	public static Vector3f toDegrees(Vector3f vector) {
		vector = new Vector3f(vector);
		vector.x = (float) Math.toDegrees(vector.x);
		vector.y = (float) Math.toDegrees(vector.y);
		vector.z = (float) Math.toDegrees(vector.z);
		return vector;
	}
}

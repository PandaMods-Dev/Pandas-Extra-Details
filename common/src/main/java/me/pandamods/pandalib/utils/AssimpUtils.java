package me.pandamods.pandalib.utils;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.assimp.*;

public class AssimpUtils {
	@SuppressWarnings("unused")
	public static Matrix4f toMatrix4f(AIMatrix4x4 aiMatrix) {
		return new Matrix4f(
				aiMatrix.a1(), aiMatrix.b1(), aiMatrix.c1(), aiMatrix.d1(),
				aiMatrix.a2(), aiMatrix.b2(), aiMatrix.c2(), aiMatrix.d2(),
				aiMatrix.a3(), aiMatrix.b3(), aiMatrix.c3(), aiMatrix.d3(),
				aiMatrix.a4(), aiMatrix.b4(), aiMatrix.c4(), aiMatrix.d4()
		);
	}

	@SuppressWarnings("unused")
	public static Matrix3f toMatrix3f(AIMatrix3x3 aiMatrix) {
		return new Matrix3f(
				aiMatrix.a1(), aiMatrix.b1(), aiMatrix.c1(),
				aiMatrix.a2(), aiMatrix.b2(), aiMatrix.c2(),
				aiMatrix.a3(), aiMatrix.b3(), aiMatrix.c3()
		);
	}

	@SuppressWarnings("unused")
	public static Vector3f toVector3f(AIVector3D vector) {
		return new Vector3f(vector.x(), vector.y(), vector.z());
	}

	@SuppressWarnings("unused")
	public static Quaternionf toQuaternionf(AIQuaternion quaternion) {
		return new Quaternionf(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
	}
}

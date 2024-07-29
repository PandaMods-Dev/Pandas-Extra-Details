package me.pandamods.pandalib.utils;

import org.jetbrains.annotations.ApiStatus;
import org.joml.*;

import java.text.NumberFormat;

public class PrintUtils {
	// Vector2
	@SuppressWarnings("unused")
	public static String getVector2String(Vector2fc vector) {
		return getVector2String(vector.x(), vector.y());
	}

	@SuppressWarnings("unused")
	public static String getVector2String(Vector2dc vector) {
		return getVector2String(vector.x(), vector.y());
	}

	@SuppressWarnings("unused")
	public static String getVector2String(Vector2ic vector) {
		return getVector2String(vector.x(), vector.y());
	}

	@SuppressWarnings("unused")
	public static String getVector2String(double x, double y) {
		return "Vector2[" + x + ", " + y + "]";
	}

	// Vector3
	@SuppressWarnings("unused")
	public static String getVector3String(Vector3fc vector) {
		return getVector3String(vector.x(), vector.y(), vector.z());
	}

	@SuppressWarnings("unused")
	public static String getVector3String(Vector3dc vector) {
		return getVector3String(vector.x(), vector.y(), vector.z());
	}

	@SuppressWarnings("unused")
	public static String getVector3String(Vector3ic vector) {
		return getVector3String(vector.x(), vector.y(), vector.z());
	}

	@SuppressWarnings("unused")
	public static String getVector3String(double x, double y, double z) {
		return "Vector3[" + x + ", " + y + ", " + z + "]";
	}

	// Vector4
	@SuppressWarnings("unused")
	public static String getVector4String(Vector4fc vector) {
		return getVector4String(vector.x(), vector.y(), vector.z(), vector.w());
	}

	@SuppressWarnings("unused")
	public static String getVector4String(Vector4dc vector) {
		return getVector4String(vector.x(), vector.y(), vector.z(), vector.w());
	}

	@SuppressWarnings("unused")
	public static String getVector4String(Vector4ic vector) {
		return getVector4String(vector.x(), vector.y(), vector.z(), vector.w());
	}

	@SuppressWarnings("unused")
	public static String getVector4String(double x, double y, double z, double w) {
		return "Vector4[" + x + ", " + y + ", " + z + ", " + w + "]";
	}

	// Quaternion
	@SuppressWarnings("unused")
	public static String getQuaternionString(Quaternionfc quaternion) {
		return getQuaternionString(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
	}

	@SuppressWarnings("unused")
	public static String getQuaternionString(Quaterniondc quaternion) {
		return getQuaternionString(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
	}

	@SuppressWarnings("unused")
	private static String getQuaternionString(double x, double y, double z, double w) {
		return "Quaternion[" + x + ", " + y + ", " + z + ", " + w + "]";
	}

	// Matrix3
	@SuppressWarnings("unused")
	public static String getMatrix3String(Matrix3fc matrix) {
		return "Matrix3[" +
				"\n| Rotation (Euler): " + getVector3String(matrix.getEulerAnglesXYZ(new Vector3f())) +
				"\n| Rotation (Quaternion): " + getQuaternionString(matrix.getNormalizedRotation(new Quaternionf())) +
				"\n\\ Scale: " + getVector3String(matrix.getScale(new Vector3f())) +
				"\n]";
	}

	@SuppressWarnings("unused")
	public static String getMatrix3String(Matrix3dc matrix) {
		return "Matrix3[" +
				"\n| Rotation (Euler): " + getVector3String(matrix.getEulerAnglesXYZ(new Vector3d())) +
				"\n| Rotation (Quaternion): " + getQuaternionString(matrix.getNormalizedRotation(new Quaterniond())) +
				"\n\\ Scale: " + getVector3String(matrix.getScale(new Vector3d())) +
				"\n]";
	}

	// Matrix4
	@SuppressWarnings("unused")
	public static String getMatrix4String(Matrix4fc matrix) {
		return "Matrix4[" +
				"\n| Translation: " + getVector3String(matrix.getTranslation(new Vector3f())) +
				"\n| Rotation (Euler): " + getVector3String(matrix.getEulerAnglesXYZ(new Vector3f())) +
				"\n| Rotation (Quaternion): " + getQuaternionString(matrix.getNormalizedRotation(new Quaternionf())) +
				"\n\\ Scale: " + getVector3String(matrix.getScale(new Vector3f())) +
				"\n]";
	}

	@SuppressWarnings("unused")
	public static String getMatrix4String(Matrix4dc matrix) {
		return "Matrix4[" +
				"\n| Translation: " + getVector3String(matrix.getTranslation(new Vector3d())) +
				"\n| Rotation (Euler): " + getVector3String(matrix.getEulerAnglesXYZ(new Vector3d())) +
				"\n| Rotation (Quaternion): " + getQuaternionString(matrix.getNormalizedRotation(new Quaterniond())) +
				"\n\\ Scale: " + getVector3String(matrix.getScale(new Vector3d())) +
				"\n]";
	}
}

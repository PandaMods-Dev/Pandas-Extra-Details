package me.pandamods.pandalib.utils;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MatrixUtils {
	public static Matrix4f lerp(Matrix4f main, Matrix4f other, float alpha) {
		Vector3f mainTranslation = new Vector3f();
		Quaternionf mainRotation = new Quaternionf();
		Vector3f mainScale = new Vector3f();
		Vector3f otherTranslation = new Vector3f();
		Quaternionf otherRotation = new Quaternionf();
		Vector3f otherScale = new Vector3f();

		main.getTranslation(mainTranslation);
		main.getUnnormalizedRotation(mainRotation);
		main.getScale(mainScale);
		other.getTranslation(otherTranslation);
		other.getUnnormalizedRotation(otherRotation);
		other.getScale(otherScale);

		Vector3f lerpedTranslation = new Vector3f();
		Quaternionf lerpedRotation = new Quaternionf();
		Vector3f lerpedScale = new Vector3f();

		mainTranslation.lerp(otherTranslation, alpha, lerpedTranslation);
		mainRotation.slerp(otherRotation, alpha, lerpedRotation);
		mainScale.lerp(otherScale, alpha, lerpedScale);

		return main.identity().translationRotateScale(lerpedTranslation, lerpedRotation, lerpedScale);
	}
}

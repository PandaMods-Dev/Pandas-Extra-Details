package me.pandamods.extra_details.pandalib.utils;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class MatrixUtils {
	public static Matrix4f lerpMatrix(Matrix4f mat1, Matrix4f mat2, float alpha) {
		Vector3f translation1 = new Vector3f();
		Quaternionf rotation1 = new Quaternionf();
		Vector3f scale1 = new Vector3f();
		mat1.getTranslation(translation1);
		mat1.getUnnormalizedRotation(rotation1);
		mat1.getScale(scale1);

		Vector3f translation2 = new Vector3f();
		Quaternionf rotation2 = new Quaternionf();
		Vector3f scale2 = new Vector3f();
		mat2.getTranslation(translation2);
		mat2.getUnnormalizedRotation(rotation2);
		mat2.getScale(scale2);

		Vector3f lerpedTranslation = new Vector3f(translation1).lerp(translation2, alpha);
		Quaternionf lerpedRotation = new Quaternionf(rotation1).slerp(rotation2, alpha);
		Vector3f lerpedScale = new Vector3f(scale1).lerp(scale2, alpha);

		Matrix4f lerpedTranslationMat = new Matrix4f().translation(lerpedTranslation);
		Matrix4f lerpedRotationMat = new Matrix4f().rotate(lerpedRotation);
		Matrix4f lerpedScaleMat = new Matrix4f().scale(lerpedScale);

		return new Matrix4f(lerpedTranslationMat).mul(lerpedRotationMat).mul(lerpedScaleMat);
	}
}

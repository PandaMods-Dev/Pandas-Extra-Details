/*
 * Copyright (C) 2024 Oliver Froberg (The Panda Oliver)
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 * You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.pandamods.pandalib.utils;

import me.pandamods.pandalib.resource.model.Node;
import org.joml.*;
import org.lwjgl.assimp.*;

import java.util.Objects;

public class AssimpUtils {
	// Conversion methods
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
	public static Vector2f toVector2f(AIVector2D vector) {
		return new Vector2f(vector.x(), vector.y());
	}

	@SuppressWarnings("unused")
	public static Quaternionf toQuaternionf(AIQuaternion quaternion) {
		return new Quaternionf(quaternion.x(), quaternion.y(), quaternion.z(), quaternion.w());
	}

	// Equals methods
	@SuppressWarnings("unused")
	public static boolean AINodeEqualsAINode(AINode node1, AINode node2) {
		boolean namesEqual = Objects.equals(node1.mName().dataString(), node2.mName().dataString());
		boolean parentsEqual = (node1.mParent() != null) == (node2.mParent() != null);
		if (node1.mParent() != null && node2.mParent() != null) {
			parentsEqual = Objects.equals(node1.mParent().mName().dataString(), node2.mParent().mName().dataString());
		}
		boolean childrenEqual = node1.mNumChildren() == node2.mNumChildren();
		boolean meshesEqual = node1.mNumMeshes() == node2.mNumMeshes();
		return namesEqual && parentsEqual && childrenEqual && meshesEqual;
	}

	@SuppressWarnings("unused")
	public static boolean AINodeEqualsNode(AINode aiNode, Node node) {
		boolean namesEqual = Objects.equals(aiNode.mName().dataString(), node.getName());
		boolean parentsEqual = (aiNode.mParent() != null) == (node.getParent() != null);
		if (aiNode.mParent() != null && node.getParent() != null) {
			parentsEqual = Objects.equals(aiNode.mParent().mName().dataString(), node.getParent().getName());
		}
		boolean childrenEqual = aiNode.mNumChildren() == node.getChildren().size();
		boolean meshesEqual = aiNode.mNumMeshes() == node.getMeshIndexes().size();
		return namesEqual && parentsEqual && childrenEqual && meshesEqual;
	}
}

package me.pandamods.pandalib.resource.model;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private final String name;
	private final Node parent;
	private boolean visible = true;

	private final Matrix4f initialTransform;
	private final Matrix4f relativeTransform;

	private final List<Node> children = new ArrayList<>();
	private final List<Integer> meshIndexes = new ArrayList<>();

	public Node(String name, Matrix4f transformation, Node parent) {
		this.name = name;
		this.parent = parent;
		this.initialTransform = new Matrix4f(transformation);
		this.relativeTransform = transformation;

		if (parent != null)
			parent.children.add(this);
	}

	public Node findNode(String name) {
		for (Node child : this.children) {
			if (child.name.equals(name)) {
				return child;
			}
		}

		for (Node child : this.children) {
			Node foundNode = child.findNode(name);
			if (foundNode != null) return foundNode;
		}
		return null;
	}

	public String getName() {
		return name;
	}

	public Node getParent() {
		return parent;
	}

	public List<Node> getChildren() {
		return children;
	}

	public List<Integer> getMeshIndexes() {
		return meshIndexes;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	/**
	 * Returns the initial relative transform of this node.
	 * This transformation is the transformation of the node when it was created.
	 *
	 * @return the initial transformation of this node
	 */
	public Matrix4f getInitialTransform() {
		return initialTransform;
	}

	/**
	 * Returns the relative transformation of this node.
	 * This transformation is relative to the parent node.
	 *
	 * @return the relative transformation of this node
	 */
	public Matrix4f getRelativeTransform() {
		return relativeTransform;
	}

	/**
	 * Returns the initial global transformation of this node.
	 * This transformation is relative to the root node.
	 *
	 * @return the initial global transformation of this node
	 */
	public Matrix4f getInitialGlobalTransform() {
		Matrix4f globalTransform = new Matrix4f();
		if (getParent() != null) {
			globalTransform.mul(getParent().getInitialGlobalTransform());
		}
		globalTransform.mul(getInitialTransform());
		return globalTransform;
	}

	/**
	 * Returns the global transformation of this node.
	 * This transformation is relative to the root node.
	 *
	 * @return the global transformation of this node
	 */
	public Matrix4f getGlobalTransform() {
		Matrix4f globalTransform = new Matrix4f();
		if (getParent() != null) {
			globalTransform.mul(getParent().getGlobalTransform());
		}
		globalTransform.mul(getRelativeTransform());
		return globalTransform;
	}

	/**
	 * Sets the local transformation of this node.
	 * This transformation is relative to the parent node.
	 *
	 * @param transform the new local transformation
	 */
	public void setLocalTransform(Matrix4f transform) {
		this.initialTransform.mul(transform, this.relativeTransform);
	}

	public Matrix4f getLocalTransform() {
		return this.relativeTransform.mul(new Matrix4f(initialTransform).invert(), new Matrix4f());
	}
}

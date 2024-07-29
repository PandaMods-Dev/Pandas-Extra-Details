package me.pandamods.pandalib.resource.model;

import java.util.*;
import java.util.List;

public class Model {
	private List<Mesh> meshes = new ArrayList<>();
	private List<Node> nodes = new ArrayList<>();

	private Node rootNode;

	public Model() {}

	public Model(Node rootNode, List<Mesh> meshes, List<Node> nodes) {
		set(rootNode, meshes, nodes);
	}

	public Model set(Node rootNode, List<Mesh> meshes, List<Node> nodes) {
		this.meshes = meshes;
		this.rootNode = rootNode;
		this.nodes = nodes;
		return this;
	}

	public List<Mesh> getMeshes() {
		return meshes;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public Node getRootNode() {
		return rootNode;
	}
}

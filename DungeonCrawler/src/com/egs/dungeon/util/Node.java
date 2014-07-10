package com.egs.dungeon.util;

public class Node {

	protected Vector2i tile;
	protected Node parent;
	
	protected double fCost;
	protected double gCost;
	protected double hCost;
	
	public Node(Vector2i tile, Node parent, double gCost, double hCost){
		this.tile = tile;
		this.parent = parent;
		this.gCost = gCost;
		this.hCost = hCost;
		this.fCost = gCost + hCost;
	}

	public Vector2i getTile() {
		return tile;
	}

	public void setTile(Vector2i tile) {
		this.tile = tile;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public double getfCost() {
		return fCost;
	}

	public void setfCost(double fCost) {
		this.fCost = fCost;
	}

	public double getgCost() {
		return gCost;
	}

	public void setgCost(double gCost) {
		this.gCost = gCost;
	}

	public double gethCost() {
		return hCost;
	}

	public void sethCost(double hCost) {
		this.hCost = hCost;
	}
}

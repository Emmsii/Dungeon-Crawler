package com.egs.dungeon.util;

public class Vector2i {

	protected int x;
	protected int y;
	
	public Vector2i(){
		set(0, 0);
	}
	
	public Vector2i(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void set(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Vector2i add(Vector2i vector){
		this.x += vector.x;
		this.y += vector.y;
		return this;
	}

	public Vector2i subtract(Vector2i vector){
		this.x -= vector.x;
		this.y -= vector.y;
		return this;
	}
	
	public boolean equals(Object object){
		if(!(object instanceof Vector2i)) return false;
		Vector2i vec = (Vector2i) object;
		if(vec.getX() == this.getX() && vec.getY() == this.getY()) return true;
		return false;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}

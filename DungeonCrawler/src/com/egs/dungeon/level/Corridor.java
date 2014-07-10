package com.egs.dungeon.level;

import java.util.ArrayList;
import java.util.List;

import com.egs.dungeon.util.Coord;

public class Corridor {

	protected Room rA;
	protected Room rB;
	
	protected Coord cA;
	protected Coord cB;
	
	protected List<Coord> coords = new ArrayList<Coord>();
	
	public Corridor(Room rA, Room rB){
		this.rA = rA;
		this.rB = rB;
	}
	
	public Corridor(Coord cA, Coord cB){
		this.cA = cA;
		this.cB = cB;
	}

	public Room getrA() {
		return rA;
	}

	public void setrA(Room rA) {
		this.rA = rA;
	}

	public Room getrB() {
		return rB;
	}

	public void setrB(Room rB) {
		this.rB = rB;
	}
	protected void addCoord(Coord c){
		coords.add(c);
	}
	
	public Coord getCoord(Coord coord){
		for(Coord c : coords) if(c.equals(coord)) return coord;
		return null;
	}
	
	public Coord getCoord(int x, int y){
		for(Coord c : coords) if(c.getX() == x&& c.getY() == y) return c;
		return null;		
	}
	
	public List<Coord> getCoords() {
		return coords;
	}

	public void setCoords(List<Coord> coords) {
		this.coords = coords;
	}

	public Coord getcA() {
		return cA;
	}

	public void setcA(Coord cA) {
		this.cA = cA;
	}

	public Coord getcB() {
		return cB;
	}

	public void setcB(Coord cB) {
		this.cB = cB;
	}
}

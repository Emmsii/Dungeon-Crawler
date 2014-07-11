package com.egs.dungeon.level;

import java.util.ArrayList;
import java.util.List;

import com.egs.dungeon.util.Coord;

public class Room {

	protected int id;
	protected int x;
	protected int y;
	protected int width;
	protected int height;
		
	protected boolean hasStart;
	protected boolean hasExit;
	protected boolean isSpecial;
	protected boolean isSettlement;
	
	protected List<Coord> coords = new ArrayList<Coord>();
	protected List<Coord> connectPoints = new ArrayList<Coord>();
	
	protected int[][] roomData;
		
	public Room(int id, int x, int y, int width, int height, boolean isSpecial, boolean isSettlement){
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isSpecial = isSpecial;
		this.isSettlement = isSettlement;
		roomData = new int[width][height];

		hasStart = false;
		hasExit = false;
		
	}

	public boolean collidesWith(Room r){
		int tw = this.width;
		int th = this.height;
		int rw = r.getWidth();
		int rh = r.getHeight();
		
		if(rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) return false;

		int tx = this.x;
		int ty = this.y;
		int rx = r.getX();
		int ry = r.getY();
		rw += rx;
		rh += ry;
		tw += tx;
		th += ty;
		
		return ((rw <= rx || rw >= tx) && (rh <= ry || rh >= ty) && (tw <= tx || tw >= rx) && (th <= ty || th >= ry));
	}
	
	public int getCenterX(){
		return x + (width / 2);
	}
	
	public int getCenterY(){
		return y + (height / 2);
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

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isHasStart() {
		return hasStart;
	}

	public void setHasStart(boolean hasStart) {
		this.hasStart = hasStart;
	}

	public boolean isHasExit() {
		return hasExit;
	}

	public void setHasExit(boolean hasExit) {
		this.hasExit = hasExit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int[][] getRoomData() {
		return roomData;
	}
	
	public int getRoomData(int x, int y){
		return roomData[x][y];
	}

	public void setRoomData(int[][] roomData) {
		this.roomData = roomData;
	}

	public boolean isSpecial() {
		return isSpecial;
	}

	public void setSpecial(boolean isSpecial) {
		this.isSpecial = isSpecial;
	}

	public boolean isSettlement() {
		return isSettlement;
	}

	public void setSettlement(boolean isSettlement) {
		this.isSettlement = isSettlement;
	}

	public void addConnectPoint(Coord c){
		connectPoints.add(c);
	}
	
	public List<Coord> getConnectPoints() {
		return connectPoints;
	}

	public void setConnectPoint(List<Coord> connect) {
		this.connectPoints = connect;
	}
}

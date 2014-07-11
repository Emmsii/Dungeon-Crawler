package com.egs.dungeon.level;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.egs.dungeon.Game;
import com.egs.dungeon.util.Coord;
import com.egs.dungeon.util.FileHandler;
import com.egs.dungeon.util.Node;
import com.egs.dungeon.util.Noise;
import com.egs.dungeon.util.Vector2i;

public class Dungeon {
	
	private FileHandler file;
	private Noise noise;
	private Random random;
	private Game game;
	
	protected int size;
	protected long seed;
	protected int level;
	
	protected List<Room> rooms;
	protected List<Corridor> corridors;
	
	protected int maxRooms;
	protected int maxSpecialRooms;
	protected int specialRooms;
	
	protected Coord start;
	protected Coord end;
	
	protected int[][] tiles;	
	
	protected boolean accepted;
	
	public Dungeon(int size, long seed, int level, FileHandler file, Game game){
		this.size = size;
		this.seed = seed;
		this.level = level;
		this.file = file;
		this.game = game;
		
//		maxRooms = 60;
		maxRooms = (int) (size * 0.3);
		maxSpecialRooms = 8;
		
		tiles = new int[size][size];
	}

	public void init(){
		random = new Random(seed);
		noise = new Noise();
		
		accepted = false;
		generateDungeon();
//		while(!accepted){
//			generateDungeon();
//			checkDungeon();
//		}
	}
	
	private void checkDungeon(){
		List<Node> path = game.pathFind(new Vector2i(start.getX(), start.getY()), new Vector2i(end.getX(), end.getY()), 0);
		if(path != null) accepted = true;
	}
		
	/*
	 * Dungeon Generation Methods
	 */
	
	public void generateDungeon(){
		double startTime = System.currentTimeMillis();
		System.out.println("Generating dungeon...");
		System.out.println("Seed: " + seed);
		
		for(int y = 0; y < size; y++){
			for(int x = 0; x < size; x++){
				tiles[x][y] = 0;
			}
		}
		
		rooms = null;
		corridors = null;
		start = null;
		end = null;
		rooms = new ArrayList<Room>();
		corridors = new ArrayList<Corridor>();
		
		file.loadRoomTileData("untitled");
		
		placeStartAndEnd();
//		for(int i = 0; i < maxSpecialRooms; i++) placeSpecialRooms();
		placeSpecialRooms();
		placeSettlement();
		 
		while(rooms.size() < maxRooms){
			int randomWidth = random.nextInt(15) + 6;
			int randomHeight = random.nextInt(15) + 6;
			int randomX = random.nextInt((size - 10) - 5) + 5;
			int randomY = random.nextInt((size - 10) - 5) + 5;
			if(!checkBounds(randomX + randomWidth, randomY + randomHeight)) continue;
			Room possibleRoom = new Room(rooms.size(), randomX, randomY, randomWidth, randomHeight, false, false);
			if(roomCollides(possibleRoom)) continue;
			rooms.add(possibleRoom);
		}
		for(Room r : rooms) makeRoom(r);

		for(int i = 0; i < rooms.size(); i++){
			Room room = rooms.get(i);
			for(Coord c : room.getConnectPoints()){
				Coord closestPoint = findClosestPoint(c, room);
				corridors.add(new Corridor(c, closestPoint));
			}
		}
		
		for(Corridor c : corridors) makeCorridor(c);
		
		placeWalls();

//		for(Room r : rooms){
//			for(Coord c : r.getConnectPoints()){
//				System.out.println(c.getX() + ", " + c.getY());
//				tiles[c.getX()][c.getY()] = 7;
//			}
//		}
		
		System.out.println("Dungeon generated in: " + (System.currentTimeMillis() - startTime) + "ms");
	}
	
	private void placeStartAndEnd(){		
		Room startRoom = new Room(0, 5, 5, 11, 11, false, false);
		Room endRoom = new Room(1, size - (11 + 5), size - (11 + 5), 11, 11, false, false);
		
		start = new Coord(5 + (11 / 2), 5 + (11 / 2));
		end = new Coord(size - (11 + 5) + (11 / 2), size - (11 + 5) + (11 / 2));

		startRoom.addConnectPoint(new Coord(11 / 2, 11));
		endRoom.addConnectPoint(new Coord(11, 11 / 2));
		
		startRoom.setHasStart(true);
		endRoom.setHasExit(true);
		rooms.add(startRoom);
		rooms.add(endRoom);
	}
	
	private void placeSpecialRooms(){
		String name = "rooms/room2";
		int r = random.nextInt(file.getRoomNumber()) + 1;
		//TODO: random select rooms here
		int tileData[][] = file.loadRoomTileData(name);
		int connectData[][] = file.loadConnectData(name);
		if(tileData == null || connectData == null) return;
		
		int width = tileData.length;
		int height = tileData.length;
		
		boolean placing = true;
		while(placing){
			int xPos = random.nextInt((size - 10) - 5) + 5;
			int yPos = random.nextInt((size - 10) - 5) + 5;
			if(!checkBounds(xPos + width, yPos + height)) continue;
			Room specialRoom = new Room(rooms.size(), xPos, yPos, width, height, true, false);
			if(roomCollides(specialRoom)) continue;
			for(int y = 0; y < height; y++) for(int x = 0; x < width; x++) if(connectData[x][y] == 3) specialRoom.addConnectPoint(new Coord(x + xPos, y + yPos));
			System.out.println(width + ", " + height);
			specialRoom.setRoomData(tileData);
			rooms.add(specialRoom);
			specialRoom = null;
			placing = false;
		}
	}
	
	private void placeSettlement(){
		System.out.println("placing settlement");
		String name = "settlements/dwarf";
		int r = random.nextInt(file.getRoomNumber()) + 1;
		//TODO: random select rooms here
		int tileData[][] = file.loadRoomTileData(name);
		int connectData[][] = file.loadConnectData(name);
		if(tileData == null || connectData == null) return;
		
		int width = tileData.length;
		int height = tileData.length;
		
		boolean placing = true;
		while(placing){
			int xPos = random.nextInt((size - 10) - 5) + 5;
			int yPos = random.nextInt((size - 10) - 5) + 5;
			if(!checkBounds(xPos + width, yPos + height)) continue;
			Room settlement = new Room(rooms.size(), xPos, yPos, width, height, true, true);
			if(roomCollides(settlement)) continue;
			for(int y = 0; y < height; y++) for(int x = 0; x < width; x++) if(connectData[x][y] == 3) settlement.addConnectPoint(new Coord(x + xPos, y + yPos));
			System.out.println(width + ", " + height);
			settlement.setRoomData(tileData);
			rooms.add(settlement);
			settlement = null;
			placing = false;
			System.out.println("settlement placed");

		}
	}
	
	
	private void makeRoom(Room r){
		if(!checkBounds(r.getX() + r.getWidth(), r.getY() + r.getHeight())) return;
		
		if(r.isSpecial()){
			for(int y = 0; y < r.getHeight(); y++){
				for(int x = 0; x < r.getWidth(); x++){
					tiles[x + r.getX()][y + r.getY()] = r.getRoomData(x, y);
					r.addCoord(new Coord(x, y));
				}
			}
		}else{
			int possibleConnections = random.nextInt(4) + 1;
			int dir = 0;
			
			for(int i = 0; i < possibleConnections; i++){
				dir = random.nextInt(4);
				switch(dir){
					case 0:
						r.addConnectPoint(new Coord(r.getX() - 1, r.getCenterY()));
						break;
					case 1:
						r.addConnectPoint(new Coord(r.getX() + r.getWidth(), r.getCenterY()));
						break;
					case 2:
						r.addConnectPoint(new Coord(r.getCenterX(), r.getY() + r.getHeight() ));
						break;
					case 3: 
						r.addConnectPoint(new Coord(r.getCenterX(), r.getY() - 1));
						break;
				}
			}
			
			for(int y = r.getY(); y < r.getY() + r.getHeight(); y++){
				for(int x = r.getX(); x < r.getX() + r.getWidth(); x++){
					tiles[x][y] = 1;
					r.addCoord(new Coord(x, y));
					if(x == r.getCenterX() && y == r.getCenterY()) tiles[x][y] = 3;
				}
			}
			
			if(r.hasStart) tiles[start.getX()][start.getY()] = 4;
			if(r.hasExit) tiles[end.getX()][end.getY()] = 5;
		}
		
	}
	
	private void makeCorridor(Corridor c){
		Coord cA = c.getcA();
		Coord cB = c.getcB();
		
		int xa = cA.getX();
		int ya = cA.getY();
		int xb = cB.getX();
		int yb = cB.getY();
		
		while(xa != xb){
			tiles[xa][ya] = 1;
			xa += xa < xb ? 1 : -1;
		}
		
		while(ya != yb){
			tiles[xa][ya] = 1;
			ya += ya < yb ? 1 : -1;
		}
	}
	
	private void placeWalls(){
		for(int y = 0; y < size; y++){
			for(int x = 0; x < size; x++){
				if(tiles[x][y] == 0 && checkNeighboursFor(x, y, 1)) tiles[x][y] = 2;
			}
		}
	}
	
	/*
	 * Utility Methods
	 */

	public boolean checkBounds(int x, int y){
		if(x < 0 || y < 0 || x >= size || y >= size) return false;
		else return true;
	}

	private boolean roomCollides(Room room){
		for(Room r : rooms) if(room.collidesWith(r)) return true;
		return false;
	}
	
	private boolean checkNeighboursFor(int x, int y, int id){
		if(x > 0 && tiles[x - 1][y] == id) return true;
		if(x < size - 1 && tiles[x + 1][y] == id) return true;
		if(y > 0 && tiles[x][y - 1] == id) return true;
		if(y < size - 1 && tiles[x][y + 1] == id) return true;
		if(x > 0 && y > 0 && tiles[x - 1][y - 1] == id) return true;
		if(x < size - 1 && y > 0 && tiles[x + 1][y - 1] == id) return true;
		if(x > 0 && y < size - 1 && tiles[x - 1][y + 1] == id) return true;
		if(x < size - 1 && y < size - 1 && tiles[x + 1][y + 1] == id) return true;
		else return false;
	}
		
	private Coord findClosestPoint(Coord coord, Room room){
		double shortestDistance = 100;
		Coord possibleCoord = null;
		for(Room r : rooms){
			if(r.getId() == room.getId()) continue;
			for(Coord c : r.getConnectPoints()){
				if(c.getX() == coord.getX() && c.getY() == coord.getY()) continue;
				double dx = c.getX() - coord.getX();
				double dy = c.getY() - coord.getY();
				double distance = Math.sqrt(dx * dx + dy * dy);
				if(distance < shortestDistance){
					shortestDistance = distance;
					possibleCoord = c;
				}
			}
		}
		return possibleCoord;
	}
	
	private int randomRange(int a, int b){
		return random.nextInt((a - b) + 1) + b;
	}
	
	/*
	 * Getters and Setters
	 */
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Coord getStart(){
		return start;
	}
	
	public Coord getEnd(){
		return end;
	}
	
	public int getTile(int x, int y){
		return tiles[x][y];
	}
	
	public int[][] getTiles() {
		return tiles;
	}

	public void setTiles(int[][] tiles) {
		this.tiles = tiles;
	}

}

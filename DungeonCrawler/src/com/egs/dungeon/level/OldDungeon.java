package com.egs.dungeon.level;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import com.egs.dungeon.util.Coord;
import com.egs.dungeon.util.FileHandler;
import com.egs.dungeon.util.Noise;

public class OldDungeon {

	private Random random;
	private Noise noise;
	private FileHandler file;
	
	protected long seed;
	
	protected int size;
	protected int level;
	
	protected int[][] tiles;
	protected boolean[][] los;
		
	protected int maxRooms;
	protected int maxSpecialRooms;
	protected int roomMax = 20;
	protected int roomMin = 5;
		
	protected int startX;
	protected int startY;
	protected int endX;
	protected int endY;
	
	protected List<Room> rooms;
	protected List<Corridor> corridors;
		
	public OldDungeon(int size, long seed, int level, FileHandler file){
		random = new Random();
		noise = new Noise();
		this.size = size;
		this.seed = seed;
		this.level = level;
		this.file = file;
		maxRooms = (int) (size * 0.8);
		maxSpecialRooms = (int) (size * 0.45);
		tiles = new int[size][size];
		los = new boolean[size][size];
	}
	
	public void init(){
		for(int y = 0; y < size; y++){
			for(int x = 0; x < size; x++){
				tiles[x][y] = 0;
			}
		}
		generateDungeon();
	}
	
	private void generateDungeon(){
		double start = System.currentTimeMillis();
		System.out.println("Generating Dungeon...");
		rooms = null;
		corridors = null;
		rooms = new ArrayList<Room>();
		corridors = new ArrayList<Corridor>();
				
		makeStartAndEndRooms();
//		for(int i = 0; i < maxSpecialRooms; i++) placeSpecialRooms();
				
		while(rooms.size() < maxRooms){
			int rWidth = randomRange(roomMax, roomMin);
			int rHeight = randomRange(roomMax, roomMin);
			int xPos = random.nextInt((size - 10) - 5) + 5;
			int yPos = random.nextInt((size - 10) - 5) + 5;
					
			if(!checkBounds(xPos + rWidth, yPos + rHeight)) continue;
			Room testRoom = new Room(rooms.size(), xPos, yPos, rWidth, rHeight, false);
			if(!roomCollides(testRoom)){
				int possibleConnections = random.nextInt(4) + 1;
				int dir = 0;
				for(int i = 0; i < possibleConnections; i++){
					dir = random.nextInt(4);
					switch(dir){
						case 0:
							testRoom.addConnectPoint(new Coord(testRoom.getX(), testRoom.getCenterY()));
							break;
						case 1:
							testRoom.addConnectPoint(new Coord(testRoom.getX() + testRoom.getWidth(), testRoom.getCenterY()));
							break;
						case 2:
							testRoom.addConnectPoint(new Coord(testRoom.getCenterX(), testRoom.getY() + testRoom.getHeight()));
							break;
						case 3: 
							testRoom.addConnectPoint(new Coord(testRoom.getCenterX(), testRoom.getY()));
							break;
						default:
							testRoom.addConnectPoint(new Coord(testRoom.getX(), testRoom.getCenterY()));
							break;	
					}
				}
				rooms.add(testRoom);
			}
		}
		
//		for(int i = 0; i < rooms.size(); i++){
//			if(rooms.get(i).getConnections() == 0 && rooms.get(i).getConnections() < 2){
//				if(rooms.get(i).hasExit || rooms.get(i).hasStart){
//					int j = randomRange(rooms.size(), 1);
//					corridors.add(new Corridor(rooms.get(i), rooms.get((i + j) % rooms.size())));
//				}else if(random.nextInt(10) <= 3){
//					int j = randomRange(rooms.size(), 1);
//					corridors.add(new Corridor(rooms.get(i), rooms.get((i + j) % rooms.size())));
//				}
//			}
//		}
		
		for(Corridor c : corridors) makeCorridor(c);
//		for(Room r : rooms) if(r.getConnections() >= 1) makeRoom(r);
		
		for(ListIterator<Room> i = rooms.listIterator(); i.hasNext();){
			Room r = i.next();
//			if(r.getConnections() < 1){
//				i.remove();
//				continue;
//			}
		}
		
		placeStartAndEnd();
		makeWalls();
		//placeDoors();
		//placeObjects();
		//makeCaves2();
		//makeCaves();
		rooms = null;
		corridors = null;
		System.out.println("Dungeon generated in: " + (System.currentTimeMillis() - start) + "ms");
	}
	
	private void makeStartAndEndRooms(){
		Room startRoom = new Room(0, 5, 5, 13, 13, false);
		Room endRoom = new Room(1, size - (13 + 5), size - (13 + 5), 13, 13, false);
		
		startRoom.setHasStart(true);
		endRoom.setHasExit(true);
		
		rooms.add(startRoom);
		rooms.add(endRoom);
	}
	
//	private void placeSpecialRooms(){
//		String name = "testRoom";
//		int r = random.nextInt(file.getRoomNumber()) + 1;
//		String[] roomData = file.loadRoom(name + Integer.toString(r));
//				
//		int width = roomData[0].length();
//		int height = roomData.length;
//		
//		int[][] finalData = new int[width][height];
//		
//		for(int y = 0; y < height; y++){
//			for(int x = 0; x < width; x++){
//				if(roomData[y].substring(x, x + 1) != null) finalData[x][y] = Integer.parseInt(roomData[y].substring(x, x + 1));
//			}
//		}
//		
//		boolean placing = true;
//		int tries = 0;
//		while(placing){
//			tries++;
//			if(tries > 100) break;
//			int xPos = random.nextInt((size - 10) - 5) + 5;
//			int yPos = random.nextInt((size - 10) - 5) + 5;
//			boolean collides = false;
//			for(Room room : rooms) for(Coord c : room.getCoords()){
//				if(c.getX() == xPos - 2 && c.getY() == yPos - 2) collides = true;
//				if(c.getX() == xPos + 2 && c.getY() == yPos + 2) collides = true;
//			}
//			if(collides) continue;
//			if(!checkBounds(xPos + width, yPos + height)) continue;
//			Room testRoom = new Room(rooms.size(), xPos, yPos, width, height, true);
//			if(!roomCollides(testRoom)) {
//				testRoom.setRoomData(finalData);
//				int possibleConnections = random.nextInt(4) + 1;
//				int dir = 0;
//				for(int i = 0; i < possibleConnections; i++){
//					dir = random.nextInt(4);
//					switch(dir){
//						case 0:
//							testRoom.addConnectPoint(new Coord(testRoom.getX(), testRoom.getCenterY()));
//							break;
//						case 1:
//							testRoom.addConnectPoint(new Coord(testRoom.getX() + testRoom.getWidth(), testRoom.getCenterY()));
//							break;
//						case 2:
//							testRoom.addConnectPoint(new Coord(testRoom.getCenterX(), testRoom.getY() + testRoom.getHeight()));
//							break;
//						case 3: 
//							testRoom.addConnectPoint(new Coord(testRoom.getCenterX(), testRoom.getY()));
//							break;
//						default:
//							testRoom.addConnectPoint(new Coord(testRoom.getX(), testRoom.getCenterY()));
//							break;	
//					}
//				}
//				rooms.add(testRoom);
//				placing = false;
//			}
//		}
//		
//	}

	private void placeStartAndEnd(){
		Room startRoom = rooms.get(0);
		Room endRoom = rooms.get(1);
		
		int sX = startRoom.getCenterX();
		int sY = startRoom.getCenterY();
		int eX = endRoom.getCenterX();
		int eY = endRoom.getCenterY();
		
		startX = sX;
		startY = sY;
		endX = eX;
		endY = eY;

		tiles[sX][sY] = 4;
		tiles[eX][eY] = 5;
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
			for(int y = r.getY(); y < r.getY() + r.getHeight(); y++){
				for(int x = r.getX(); x < r.getX() + r.getWidth(); x++){
					tiles[x][y] = 1;
					r.addCoord(new Coord(x, y));
				}
			}
		}
		
	}
	
	private void makeCorridor(Corridor c){
		Room rA = c.getrA();
		Room rB = c.getrB();
		
		if(rA.getConnectPoints().size() == 0){
			rooms.remove(rA);
			return;
		}
		if(rB.getConnectPoints().size() == 0){
			rooms.remove(rB);
			return;
		}
		
		int randomConnectionA = random.nextInt(rA.getConnectPoints().size());
		int randomConnectionB = random.nextInt(rB.getConnectPoints().size());
		
		Coord connectA = rA.getConnectPoints().get(randomConnectionA);
		Coord connectB = rB.getConnectPoints().get(randomConnectionB);
			
		int xa = connectA.getX();
		int ya = connectA.getY();
		int xb = connectB.getX();
		int yb = connectB.getY();
		
		
		boolean broken = false;
		
		while(xa != xb){
			tiles[xa][ya] = 1;
			c.addCoord(new Coord(xa, ya));
			xa += xa < xb ? 1 : -1;
			if(tiles[xa][ya] == 1){
				broken = true;
				break;
			}
		}
		
		while(ya != yb){
			tiles[xa][ya] = 1;
			c.addCoord(new Coord(xa, ya));
			ya += ya < yb ? 1 : -1;
			if(tiles[xa][ya] == 1){
				broken = true;
				break;
			}
		}
		
		if(broken) c.getCoords().clear();
		
//		rA.connections++;
//		if(!broken) rB.connections++;
	}
	
	private void makeWalls(){
		for(int y = 0; y < size; y++){
			for(int x = 0; x < size; x++){
				if(tiles[x][y] == 0 && checkFloor(x, y, 1)) tiles[x][y] = 2;
			}
		}
	}
	
	private void placeDoors(){
		for(int y = 0; y < size; y++){
			for(int x = 0; x < size; x++){
				if(tiles[x][y] == 1){
					if(!checkBounds(x - 1, y)) continue;
					if(!checkBounds(x + 1, y)) continue;
					if(!checkBounds(x - 1, y - 1)) continue;
					if(!checkBounds(x + 1, y - 1)) continue;
					if(!checkBounds(x - 1, y + 1)) continue;
					if(!checkBounds(x + 1, y + 1)) continue;
					if(!checkBounds(x, y - 1)) continue;
					if(!checkBounds(x, y + 1)) continue;
					
					if(tiles[x - 1][y] == 2 && tiles[x + 1][y] == 2 && tiles[x - 1][y - 1] == 1 && tiles[x + 1][y - 1] == 1){
						if(!checkFloor(x, y, 3)) if(random.nextInt(10) <= 3) tiles[x][y] = 3;
					}
					
					if(tiles[x - 1][y] == 2 && tiles[x + 1][y] == 2 && tiles[x - 1][y + 1] == 1 && tiles[x + 1][y + 1] == 1){
						if(!checkFloor(x, y, 3)) if(random.nextInt(10) <= 3) tiles[x][y] = 3;
					}
					
					if(tiles[x][y - 1] == 2 && tiles[x][y + 1] == 2 && tiles[x - 1][y - 1] == 1 && tiles[x - 1][y + 1] == 1){
						if(!checkFloor(x, y, 3)) if(random.nextInt(10) <= 3) tiles[x][y] = 3;
					}
					
					if(tiles[x][y - 1] == 2 && tiles[x][y + 1] == 2 && tiles[x + 1][y - 1] == 1 && tiles[x + 1][y + 1] == 1){
						if(!checkFloor(x, y, 3)) if(random.nextInt(10) <= 3) tiles[x][y] = 3;
					}
				}
			}
		}
		
	}
	
	private void placeObjects(){
		//Place chests
		for(Room r : rooms){
			if(random.nextInt(10) < 3){
				int startX = r.getX();
				int startY = r.getY();
				int endX = r.getX() + r.getWidth();
				int endY = r.getY() + r.getHeight();
				
				int rSide = random.nextInt(4);
				int rX = randomRange2(startX, endX);
				int rY = randomRange2(startY, endY);
				
				if(!checkFloor(startX, rY, 3) && !checkFloor(endX, rY, 3) && !checkFloor(rX, startY, 3) && !checkFloor(startX, rY, 3)){
					if(rSide == 0) tiles[startX][rY] = 100;
					else if(rSide == 1) tiles[endX][rY] = 100;
					else if(rSide == 2) tiles[rX][startY] = 100;
					else if(rSide == 3) tiles[startX][rY] = 100;
				}
			}
		}
	}


	
	private void makeCaves2(){
		int[][] maskMap = noise.startNoise(size, size, seed * 2, 0.05, 0.4, 8, 16);
		boolean[][] cellMap = new boolean [size][size];
		float chanceToStartAlive = 0.45f;
		
		for(int y = 0; y < size; y++){
			for(int x = 0; x < size; x++){
				if(random.nextFloat() < chanceToStartAlive) cellMap[x][y] = true;
				else cellMap[x][y] = false;
			}
		}
		for(int i = 0; i < 6; i++) cellMap = caveStep(cellMap);
		for(int y = 0; y < size; y++) for(int x = 0; x < size; x++) if(maskMap[x][y] < 150) cellMap[x][y] = false;
		for(int i = 0; i < 6; i++) cellMap = caveStep(cellMap);
		for(int y = 0; y < size; y++) for(int x = 0; x < size; x++) if(cellMap[x][y]) tiles[x][y] = 8;
	}
	
	private boolean[][] caveStep(boolean[][] oldMap){
		boolean[][] newMap = new boolean[size][size];
		int deathLimit = 3;
		int birthLimit = 4;
		
		for(int x = 0; x < oldMap.length; x++){
			for(int y = 0; y < oldMap[0].length; y++){
				int nbs = countAliveNeighbours(oldMap, x, y);
				if(oldMap[x][y]){
					if(nbs < deathLimit) newMap[x][y] = false;
					else newMap[x][y] = true;
				}else{
					if(nbs > birthLimit) newMap[x][y] = true;
					else newMap[x][y] = false;
				}
			}
		}
		return newMap;
	}
	
	private int countAliveNeighbours(boolean[][] map, int x, int y){
		int count = 0;
		for(int i = -1; i < 2; i++){
			for(int j = -1; j < 2; j++){
				int neighbour_x = x + i;
				int neighbour_y = y + j;
				if(i == 0 && j == 0){
				} else if(neighbour_x < 0 || neighbour_y < 0 || neighbour_x >= size || neighbour_y >= size)count = count + 1;
				else if(map[neighbour_x][neighbour_y])count = count + 1;
			}
		}
		return count;
	}
	
	private void makeCaves(){
		int[][] maskMap = noise.startNoise(size, size, seed * 2, 0.05, 0.4, 8, 16);
		int[][] heatMap = noise.startNoise(size, size, seed, 0.05, 0.4, 8, 16);
		int[][] rockDensity = noise.startNoise(size, size, seed / 2, 0.05, 0.4, 8, 16);
		int[][] moistureMap = noise.startNoise(size, size, seed / 4, 0.02, 0.55, 8, 16);
						
		for(int y = 0; y < size; y++){
			for(int x = 0; x < size; x++){
				moistureMap[x][y] = (int) (moistureMap[x][y] - (heatMap[x][y] * 0.15) - (rockDensity[x][y] * 0.1));
				if(moistureMap[x][y] > 100){
					if(tiles[x][y] == 1) tiles[x][y] = 9;
					if(tiles[x][y] == 2) tiles[x][y] = 10;
				}	
				
				if(rockDensity[x][y] < 53){
					if(heatMap[x][y] > 180) tiles[x][y] = 8;
					if(heatMap[x][y] > 240) tiles[x][y] = 7;
				}
				
			}
		}
		
	}
	
	private boolean checkFloor(int x, int y, int id){		
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
	
	private int randomRange(int a, int b){
		return random.nextInt((a - b) + 1) + b;
	}

	private int randomRange2(int b, int a){
		return random.nextInt(a - b) + b;
	}
	
	private boolean roomCollides(Room testRoom){
		for(Room r : rooms) if(r.collidesWith(testRoom)) return true;
		return false;
	}
	
	public boolean checkBounds(int x, int y){
		if(x <= 0 || x > size - 1 || y <= 0 || y > size - 1) return false;
		else return true;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int[][] getTiles() {
		return tiles;
	}
	
	public int getTile(int x, int y){
		return tiles[x][y];
	}
	
	public void setTile(int tile, int x, int y){
		this.tiles[x][y] = tile;
	}

	public void setTiles(int[][] tiles) {
		this.tiles = tiles;
	}

	public boolean[][] getLos() {
		return los;
	}
	
	public boolean getLos(int x, int y){
		return los[x][y];
	}

	public void setLos(boolean data, int x, int y){
		los[x][y] = data;
	}
	
	public void setLos(boolean[][] los) {
		this.los = los;
	}

	public int getStartX() {
		return startX;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public int getStartY() {
		return startY;
	}

	public void setStartY(int startY) {
		this.startY = startY;
	}

	public int getEndX() {
		return endX;
	}

	public void setEndX(int endX) {
		this.endX = endX;
	}

	public int getEndY() {
		return endY;
	}

	public void setEndY(int endY) {
		this.endY = endY;
	}
}

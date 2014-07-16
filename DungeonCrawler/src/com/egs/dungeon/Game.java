package com.egs.dungeon;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import rlforj.los.IFovAlgorithm;
import rlforj.los.ShadowCasting;

import com.egs.dungeon.entities.Entity;
import com.egs.dungeon.entities.Mob;
import com.egs.dungeon.entities.Player;
import com.egs.dungeon.level.Dungeon;
import com.egs.dungeon.level.LOSBoard;
import com.egs.dungeon.level.Settlement;
import com.egs.dungeon.level.Tile;
import com.egs.dungeon.util.FileHandler;
import com.egs.dungeon.util.InputHandler;
import com.egs.dungeon.util.Node;
import com.egs.dungeon.util.Vector2i;

public class Game {

	private Random random;
	private FileHandler file;
	private InputHandler input;
	private MainComponent main;
	private IFovAlgorithm fovAl;
	private LOSBoard board;
	
	private long seed;
	
	public int xPos;
	public int yPos;
	public int xCam;
	public int yCam;
	
	public int level;
	
	public int dungeonSize = 128;
	public int tileSize = 12;
	public int tilesW;
	public int tilesH;
	
	public Player player;
	
	private Dungeon currentDungeon;
	private List<Entity> entities = new ArrayList<Entity>();
	
	private int endX;
	private int endY;
	
	@SuppressWarnings("static-access")
	public Game(long seed, MainComponent main){
		this.seed = seed;
		this.main = main;
		random = new Random(seed);
		file = new FileHandler();
		input = new InputHandler(main);
				
		tilesW = main.WIDTH / tileSize;
		tilesH = main.HEIGHT / tileSize;
				
		init();
	}

	private void init(){
		file.newWorldFolder("TEST");
		file.loadTiles();
		level = 1;
		currentDungeon = new Dungeon(dungeonSize, seed, level, file, this);
		currentDungeon.init();
		
		board = new LOSBoard(dungeonSize);
		
		//TODO: FIX ME! FOR THE LOVE OF GOD!
		fovAl = new ShadowCasting();
		
		for(int y = 0; y < dungeonSize; y++){
			for(int x = 0; x < dungeonSize; x++){
				if(Tile.tiles[currentDungeon.getTile(x, y)].isSolid()) board.setObsticle(x, y);
			}
		}
		
		player = new Player(entities.size(), currentDungeon.getStart().getX(), currentDungeon.getStart().getY(), "P", "PLAYER", 0.0, 12, this, currentDungeon, main, input, board);
		Mob dwarf = new Mob(10, "dwarf", "D", "Dwarf", 25.0, 12, 8, 8, this, currentDungeon);
		//public Mob(int id, String type, String icon, double karma, int sight, int x, int y, Game game, Dungeon dungeon){
		
		entities.add(dwarf);
		entities.add(player);
		
		endX = currentDungeon.getEnd().getX();
		endY = currentDungeon.getEnd().getY();
				
		/*
		 * TODO: Need to refine this.
		 * Should create mob based of settlement type.
		 * Which means settlement files need a type value.
		 */
				
		if(currentDungeon.getSettlement() != null){
			System.out.println("NOT NULL");
			Settlement s = currentDungeon.getSettlement();
			int currentPop = 0;
			while(currentPop < s.getMaxPop()){
				System.out.println(s.getMaxPop());
				int rx = random.nextInt(dungeonSize);
				int ry = random.nextInt(dungeonSize);
				if(rx > s.getX() && rx <= s.getX() + s.getWidth() && ry > s.getY() && ry <= s.getY() + s.getHeight()){
					System.out.println("in bounds");
					if(currentDungeon.getTile(rx, ry) != 1) continue;
					currentPop++;
					Mob mob = file.loadEntity(s.getType(), entities.size(), rx, ry, this, currentDungeon);
					mob.setSettlement(s);
					entities.add(mob);
					System.out.println("Adding mob");
					//TODO: Get mob attributes from .xml file based of settlement type. 
					//Mob mob = new Mob(entities.size(), s.getType(), ".", rx, ry, ".")
				}
			}
		}
		
//		for(Room r : currentDungeon.getRooms()){
//			if(r.isSettlement()){
//				while(currentPop < maxPop){
//					int rx = random.nextInt(dungeonSize);
//					int ry = random.nextInt(dungeonSize);
//					if(rx > r.getX() && rx <= r.getX() + r.getWidth() && ry > r.getY() && ry <= r.getY() + r.getHeight()){
//						if(currentDungeon.getTile(rx, ry) != 1) continue;
//						currentPop++;
//						Dwarf dwarf = new Dwarf(entities.size(), rx, ry, 15, "D", this, currentDungeon);
//						dwarf.setSettlement(r);
//						entities.add(dwarf);
//					}
//				}
//			}
//		}
		
		
		

	}
	
	public void nextLevel(){
		System.out.println("NEXT LEVEL!");
		level++;
		currentDungeon = null;
		entities.clear();
		entities.add(player);
		
		currentDungeon = new Dungeon(dungeonSize, seed, level, file, this);
		currentDungeon.init();
				
		player.setDungeon(null);
		player.setDungeon(currentDungeon);
		
		player.setX(currentDungeon.getStart().getX());
		player.setY(currentDungeon.getStart().getY());
		
		endX = currentDungeon.getStart().getX();
		endY = currentDungeon.getStart().getY();
	}
	
	public void update(){
		input.update();
		for(Entity e : entities) e.update();
		fovAl.visitFieldOfView(board, player.getX(), player.getY(), player.getSight());

		if(player.getX() == endX && player.getY() == endY) nextLevel();		
	}
		
	public void render(Graphics2D g){		
		xCam = xPos - (player.getX() * tileSize) + (main.getWidth() / 2);
		yCam = yPos - (player.getY() * tileSize) + (main.getHeight() / 2);
		
		int x0 = player.getX() - (tilesW / 2);
		int y0 = player.getY() - (tilesH / 2);
		int x1 = 0;
		int y1 = 0;
		
		if(x0 < 0) x0 = 0;
		if(y0 < 0) y0 = 0;
		
		if(x0 + tilesW >= dungeonSize) x1 = dungeonSize;
		else x1 = x0 + tilesW;
		if(y0 + tilesH >= dungeonSize) y1 = dungeonSize;
		else y1 = y0 + tilesH;
		for(int y = y0; y < y1; y++){
			for(int x = x0; x < x1; x++){			
				if(Tile.tiles[currentDungeon.getTile(x, y)] == null) continue;
				Tile.tiles[currentDungeon.getTile(x, y)].render(g, (x * tileSize) + xCam, (y * tileSize) + yCam, tileSize);
				
				
				if(!board.getVisited(x, y) || board.isObstacle(x, y)){
					g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
					g.fillRect((x * tileSize) + xCam, (y * tileSize) + yCam, tileSize, tileSize);
				}
			}
		}
		for(Entity e : entities) e.render(g);
	
	}
		
	private Comparator<Node> nodeSorter = new Comparator<Node>(){
		public int compare(Node n0, Node n1){
			if(n1.getfCost() < n0.getfCost()) return +1;
			if(n1.getfCost() > n0.getfCost()) return -1;
			return 0;
		}
	};
	
	public List<Node> pathFind(Vector2i start, Vector2i end, int distance){
		List<Node> openList = new ArrayList<Node>();
		List<Node> closedList = new ArrayList<Node>();
		Node current = new Node(start, null, 0, getDistance(start, end));
		openList.add(current);
		while(openList.size() > 0){
			if(distance != 0) if(openList.size() > distance) return null;
			Collections.sort(openList, nodeSorter);
			current = openList.get(0);
			if(current.getTile().equals(end)){
				List<Node> path = new ArrayList<Node>();
				while(current.getParent() != null){
					path.add(current);
					current = current.getParent();
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			openList.remove(current);
			closedList.add(current);
			for(int i = 0; i < 9; i++){
				if(i == 4) continue;
				int x = current.getTile().getX();
				int y = current.getTile().getY();
				int xi = (i % 3) - 1;
				int yi = (i / 3) - 1;
				if(x + xi < 0 || y + yi < 0 || x + xi >= dungeonSize || y + yi >= dungeonSize) continue;
				if(Tile.tiles[currentDungeon.getTile(x + xi, y + yi)].isSolid()) continue;
				if(currentDungeon.getTile(x + xi, y + yi) == 1){
					Vector2i a = new Vector2i(x + xi, y + yi);
					double gCost = current.getgCost() + (getDistance(current.getTile(), a) == 1 ? 1 : 0.95);
					double hCost = getDistance(a, end);
					Node node = new Node(a, current, gCost, hCost);
					if(vecInList(closedList, a) && gCost >= node.getgCost()) continue;
					if(!vecInList(openList, a) || gCost < node.getgCost()) openList.add(node);
				}
				
			}
		}
		return null;
	}
	
	public boolean checkForEntity(int x, int y){
		for(Entity e : entities) if(e.getX() == x && e.getY() == y) return true;
		return false;
	}
	
	public Entity checkForEntity2(int x, int y){
		for(Entity e : entities) if(e.getX() == x && e.getY() == y) return e;
		return null;
	}
	
	private boolean vecInList(List<Node> list, Vector2i vector){
		for(Node n : list) if(n.getTile().equals(vector)) return true;
		return false;
	}
	
	private double getDistance(Vector2i tile, Vector2i end){
		double dx = tile.getX() - end.getX();
		double dy = tile.getY() - end.getY();
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	private float convertToFloat(float oldValue){
		float result = ((oldValue - 1.0f)/(player.getSight() * 2 - 0.0f) + 0.0f);
		System.out.println(result);
		return result;
	}
	
	public int getTileSize() {
		return tileSize;
	}

	public void setTileSize(int tileSize) {
		this.tileSize = tileSize;
	}

	public Dungeon getCurrentDungeon() {
		return currentDungeon;
	}

	public void setCurrentDungeon(Dungeon currentDungeon) {
		this.currentDungeon = currentDungeon;
	}

	
}

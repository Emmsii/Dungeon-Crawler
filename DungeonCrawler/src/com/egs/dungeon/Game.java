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
import com.egs.dungeon.entities.Monster;
import com.egs.dungeon.entities.Player;
import com.egs.dungeon.level.LOSBoard;
import com.egs.dungeon.level.Dungeon;
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
		
		for(int y = 0; y < dungeonSize; y++){
			for(int x = 0; x < dungeonSize; x++){
				if(Tile.tiles[currentDungeon.getTile(x, y)].isSolid()) board.setObsticle(x, y);
			}
		}
		
		player = new Player(entities.size(), currentDungeon.getStart().getX(), currentDungeon.getStart().getY(), 12, "P", input, currentDungeon, this, board, main);	
				
		endX = currentDungeon.getStart().getX();
		endY = currentDungeon.getStart().getY();

		entities.add(player);
		entities.add(new Monster(entities.size(), currentDungeon.getStart().getX() + 2, currentDungeon.getStart().getY() + 2, 30, "g", this, currentDungeon));
		
		
		fovAl = new ShadowCasting();
		
		//Create dungeon.
		//Pick room from file based off seed.
		//First, place wall rooms and corner rooms.
		//Then place center rooms.
		//Place start and finish.
		//Place rooms.
		//Done
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

//		if(player.getX() == endX && player.getY() == endY) nextLevel();		
	}
		
	public void render(Graphics2D g){
		float distance = 0.0f;
		float maxDistance = 0.0f;
		
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
				/*
				 * IDs:
				 * 
				 * 00: Bedrock
				 * 01: Floor
				 * 02: Wall
				 * 03: Door
				 * 
				 * 04: START POINT
				 * 05: END POINT
				 * 
				 * 06: Water
				 * 07: Lava
				 * 
				 * 08: Cave Floor
				 * 09: Mossy Floor
				 * 10: Mossy Wall
				 * 
				 * 100: Chest
				 * 
				 */
					
//					if(dungeon.getTile(x, y) == 0) g.setColor(new Color(25, 25, 25));
//					
//					if(dungeon.getTile(x, y) == 1) g.setColor(new Color(176, 164, 151));
//					if(dungeon.getTile(x, y) == 2) g.setColor(new Color(89, 79, 69));	
//					if(dungeon.getTile(x, y) == 3) g.setColor(new Color(163, 115, 75));
//					
//					if(dungeon.getTile(x, y) == 4) g.setColor(new Color(255, 119, 0));
//					if(dungeon.getTile(x, y) == 5) g.setColor(new Color(183, 0, 255));
//					
//					if(dungeon.getTile(x, y) == 6) g.setColor(new Color(118, 169, 219));
//					if(dungeon.getTile(x, y) == 7) g.setColor(new Color(227, 68, 32));
//					if(dungeon.getTile(x, y) == 8) g.setColor(new Color(136, 153, 99));
//					if(dungeon.getTile(x, y) == 9) g.setColor(new Color(150, 181, 81));
//					if(dungeon.getTile(x, y) == 10) g.setColor(new Color(70, 84, 37));
//					
//					if(dungeon.getTile(x, y) == 100) g.setColor(new Color(89, 19, 0));
//					
//					//g.fillRect(x * tileSize + (tileSize * dungeonSize) + xPos, y * tileSize + (tileSize * dungeonSize) + yPos, tileSize, tileSize);
//					g.fillRect((x * tileSize) + xCam, (y * tileSize) + yCam, tileSize, tileSize);
////					g.fillRect(x * tileSize + (dungeon.getX() * (tileSize * dungeonSize)), y * tileSize + (dungeon.getY() * (tileSize * dungeonSize)), tileSize, tileSize);
//					
////					g.setColor(Color.WHITE);
////					g.drawRect((x * tileSize) + xPos, (y * tileSize) + yPos, tileSize, tileSize);
				
				if(Tile.tiles[currentDungeon.getTile(x, y)] == null) continue;
				Tile.tiles[currentDungeon.getTile(x, y)].render(g, (x * tileSize) + xCam, (y * tileSize) + yCam, tileSize);
				
				
				if(!board.getVisited(x, y) || board.isObstacle(x, y)){
					g.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
					g.fillRect((x * tileSize) + xCam, (y * tileSize) + yCam, tileSize, tileSize);
				}
			}
		}
		for(Entity e : entities) e.render(g);
		//player.render(g);
	
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

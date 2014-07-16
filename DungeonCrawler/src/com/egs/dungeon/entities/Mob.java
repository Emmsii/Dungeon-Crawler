package com.egs.dungeon.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

import com.egs.dungeon.Game;
import com.egs.dungeon.level.Dungeon;
import com.egs.dungeon.level.Room;
import com.egs.dungeon.level.Settlement;
import com.egs.dungeon.level.Tile;
import com.egs.dungeon.util.Node;
import com.egs.dungeon.util.Vector2i;

public class Mob extends Entity{

	protected String type;
	protected String icon;
	protected String name;
	protected double karma;
	protected int sight;
	
	protected boolean moving;
	protected boolean onScreen;
	protected int time;
	
	protected int rx;
	protected int ry;
	
	protected Settlement settlement;
	
	public Mob(int id, String type, String icon, String name, double karma, int sight, int x, int y, Game game, Dungeon dungeon){
		super(id, x, y, game, dungeon);
		this.type = type;
		this.icon = icon;
		this.name = name;
		this.karma = karma;
		this.sight = sight;
		
		moving = false;
		time = random.nextInt(60);
	}
	
	public void render(Graphics2D g){
		if(onScreen)g.setColor(new Color(198, 232, 124));
		else g.setColor(Color.BLUE);
		g.fillRect((x * game.tileSize) + game.xCam, (y * game.tileSize) + game.yCam, game.tileSize, game.tileSize);
		g.setColor(Color.WHITE);
		g.drawString(icon, (x * game.tileSize) + game.xCam + 3, (y * game.tileSize) + game.yCam + 10);
	}
	
	public void update(){
		time++;
		if(time % 60 == 0) checkIfOnScreen();
		
		if(onScreen) updateHeavy();
		else updateLite();
	}
	
	private void updateLite(){
		
	}
	
	private void updateHeavy(){
		wanderSettlement(30, settlement);
//		wander(30);
	}
	
	/*
	 * Util Methods
	 */
	
	public void move(int x, int y){
		if(!dungeon.checkBounds(x, y)) return;
		if(!Tile.tiles[dungeon.getTile(x, y)].isSolid()){
			if(!game.checkForEntity(x, y)){
				setX(x);
				setY(y);
			}else{
				moving = false;
				return;
			}
		}else return;
	}
	
	public void wander(int speed){
		if(!moving && time % speed == 0){
			int tries = 0;
			while(!moving){
				if(tries >= 50) break;
				tries++;
				rx = x + random.nextInt(10) - 5;
				ry = y + random.nextInt(10) - 5;
			
				if(!dungeon.checkBounds(rx, ry)) continue;
				if(Tile.tiles[dungeon.getTile(rx, ry)].isSolid()) continue;
				
				List<Node> path = game.pathFind(new Vector2i(x, y), new Vector2i(rx, ry), sight);
				if(path == null) continue;
				else{
					moving = true;
					tries = 0;
				}
			}
		}
		pathTo(new Vector2i(x, y), new Vector2i(rx, ry), speed);
		
	}
	
	public void wanderSettlement(int speed, Settlement s){
		if(s == null) return;
		if(!moving && time % 80 == 0){
			int tries = 0;
			while(!moving){
				tries++;
				if(tries >= 50) break;
				rx = x + random.nextInt(10) - 5;
				ry = y + random.nextInt(10) - 5;
				
				if(!dungeon.checkBounds(rx, ry)) continue;;
				if(Tile.tiles[dungeon.getTile(x, y)].isSolid()) continue;
				if(rx > s.getX() && rx <= s.getX() + s.getWidth() && ry > s.getY() && ry <= s.getY() + s.getHeight()){
					List<Node> path = game.pathFind(new Vector2i(x, y), new Vector2i(rx, ry), sight);
					if(path == null) continue;
					else{
						moving = true;
						tries = 0;
					}
				}else continue;
			}
			tries = 0;
		}

		pathTo(new Vector2i(x, y), new Vector2i(rx, ry), speed);
//		if(s == null) return;
//		int rx = 0;
//		int ry = 0;
//		if(!moving && time % speed == 0){
//			int tries = 0;
//			//TODO: FIX ME
//			while(!moving){
//				if(tries >= 50) break;
//				tries++;
//				rx = x + random.nextInt(10) - 5;
//				ry = y + random.nextInt(10) - 5;
//				
//				if(!dungeon.checkBounds(rx, ry)) continue;
//				if(Tile.tiles[dungeon.getTile(rx, ry)].isSolid()) continue;
//				if(rx > s.getX() && rx <= s.getX() + s.getWidth() && ry > s.getY() && ry <= s.getY() + s.getHeight()){
//					List<Node> path = game.pathFind(new Vector2i(x, y), new Vector2i(rx, ry), sight);
//					if(path == null) continue;
//					else{
//						moving = true;
//						tries = 0;
//					}
//				}else continue;
//			}
//			tries = 0;
//		}
//		pathTo(new Vector2i(x, y), new Vector2i(rx, ry), speed);
	}
	
	public void pathTo(Vector2i start, Vector2i end, int speed){
		List<Node> path = null;
		if(time % speed == 0) path = game.pathFind(start, end, sight);
		if(path != null){
			if(path.size() > 0){
				moving = true;
				Vector2i vec = path.get(path.size() - 1).getTile();
				if(x < vec.getX()) move(x + 1, y);
				if(x > vec.getX()) move(x - 1, y);
				if(y < vec.getY()) move(x, y + 1);
				if(y > vec.getY()) move(x, y - 1);
			}else{
				moving = false;
			}
		}
	}
	
	public void followMov(Mob target, int speed){
		pathToEntity(new Vector2i(x, y), new Vector2i(target.getX(), target.getY()), speed);
	}
	
	public void pathToEntity(Vector2i start, Vector2i end, int speed){
		List<Node> path = null;
		if(time % speed == 0) path = game.pathFind(start, end, sight);
		
		if(path != null){
			if(path.size() > 0){
				moving = true;
				Vector2i vec = path.get(path.size() - 1).getTile();
				if(x < vec.getX()) move(x + 1, y);
				if(x > vec.getX()) move(x - 1, y);
				if(y < vec.getY()) move(x, y + 1);
				if(y > vec.getY()) move(x, y - 1);
			}else{
				moving = false;
			}
		}
	}
	
	public void checkIfOnScreen(){
		if(x > game.player.getX() - (game.tilesW / 2) && x <= game.player.getX() + (game.tilesW / 2) && y > game.player.getY() - (game.tilesH / 2) && y <= game.player.getY() + (game.tilesH / 2)) onScreen = true;
		else onScreen = false;
	}
	
	public void addKarma(double karma){
		//TODO: Add max/min karma values;
		if(this.karma + karma >= 100.0) this.karma = 100.0;
		else this.karma += karma;
	}
	
	public void removeKarma(double karma){
		if(this.karma - karma <= -100.0) this.karma = -100.0;
		else this.karma -= karma;
	}
	
	/*
	 * Getters and Setters
	 */

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getKarma() {
		return karma;
	}

	public void setKarma(double karma) {
		this.karma = karma;
	}

	public int getSight() {
		return sight;
	}

	public void setSight(int sight) {
		this.sight = sight;
	}

	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public boolean isOnScreen() {
		return onScreen;
	}

	public void setOnScreen(boolean onScreen) {
		this.onScreen = onScreen;
	}

	public Settlement getSettlement() {
		return settlement;
	}

	public void setSettlement(Settlement settlement) {
		this.settlement = settlement;
	}

}

package com.egs.dungeon.entities;

import java.util.List;

import com.egs.dungeon.Game;
import com.egs.dungeon.level.Dungeon;
import com.egs.dungeon.level.Tile;
import com.egs.dungeon.util.Node;
import com.egs.dungeon.util.Vector2i;

public class Mob extends Entity{
	
	protected boolean moving;
	protected int moveX;
	protected int moveY;
	protected int time;
	
	protected double karma;
	protected int sight;
	
	protected String icon;
	
	protected boolean onScreen;
	
	protected int rx;
	protected int ry;
	
	protected Mob(int id, int x, int y, int sight, String icon, Game game, Dungeon dungeon) {
		super(id, x, y, game, dungeon);
		this.sight = sight;
		this.icon = icon;
		
		moving = false;
		karma = 0.0;
		
	}

	public void wander(int speed){
		if(!moving && time % 80 == 0){
			int tries = 0;
			while(!moving){
				tries++;
				if(tries >= 50) break;
				rx = x + random.nextInt(10) - 5;
				ry = y + random.nextInt(10) - 5;
				
				if(!dungeon.checkBounds(rx, ry)) continue;;
				if(Tile.tiles[dungeon.getTile(x, y)].isSolid()) continue;
				
				List<Node> path = game.pathFind(new Vector2i(x, y), new Vector2i(rx, ry), sight);
				if(path == null) continue;
				else{
					moving = true;
					tries = 0;
				}
			}
			tries = 0;
		}

		pathTo(new Vector2i(x, y), new Vector2i(rx, ry), speed);
	}
	
	public void followMob(Mob target, int speed){
		pathToEntity(new Vector2i(x, y), new Vector2i(target.getX(), target.getY()), speed);
	}
	
	private void pathTo(Vector2i start, Vector2i target, int speed){
		List<Node> path = null;
		if(time % speed == 0) path = game.pathFind(start, target, sight);
		
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
	
	private void pathToEntity(Vector2i start, Vector2i target, int speed){
		List<Node> path = null;
		if(time % speed == 0) path = game.pathFind(start, target, sight);
		
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
	
	public void move(int x, int y){
		if(!dungeon.checkBounds(x, y)) return;
		if(!Tile.tiles[dungeon.getTile(x, y)].isSolid()){
			if(!game.checkForEntity(x, y)){
				setX(x);
				setY(y);
			}else{
				return;
			}
		}else{
			return;
		}
	}
	
	public void checkIfOnScreen(){
		if(x > game.xPos && x < game.xPos + game.tilesW && y > game.yPos && y < game.yPos + game.tilesH) onScreen = true;
		else onScreen = false;
	}
	
	public boolean isMoving() {
		return moving;
	}

	public void setMoving(boolean moving) {
		this.moving = moving;
	}

	public double getKarma() {
		return karma;
	}

	public void setKarma(double karma) {
		this.karma = karma;
	}

	public void addKarma(double karma){
		if(this.karma + karma >= 100.0) this.karma = 100.0;
		else this.karma += karma;
	}
	
	public void removeKarma(double karma){
		if(this.karma - karma <= -100.0) this.karma = -100.0;
		else this.karma -= karma;
	}
	
	public int getSight() {
		return sight;
	}

	public void setSight(int sight) {
		this.sight = sight;
	}

	public boolean isOnScreen() {
		return onScreen;
	}

	public void setOnScreen(boolean onScreen) {
		this.onScreen = onScreen;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
}

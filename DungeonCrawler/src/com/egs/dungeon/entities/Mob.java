package com.egs.dungeon.entities;

import java.awt.Graphics2D;

import com.egs.dungeon.Game;
import com.egs.dungeon.level.Dungeon;
import com.egs.dungeon.level.Tile;

public class Mob extends Entity{

	protected String type;
	protected String icon;
	protected double karma;
	protected int sight;
	
	protected boolean moving;
	protected boolean onScreen;
	protected int time;
	
	public Mob(int id, String type, String icon, double karma, int sight, int x, int y, Game game, Dungeon dungeon){
		super(id, x, y, game, dungeon);
		this.type = type;
		this.icon = icon;
		this.karma = karma;
		this.sight = sight;
		
		moving = false;
		time = random.nextInt(60);
	}
	
	public void render(Graphics2D g){
		
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
		
	}
	
	/*
	 * Util Methods
	 */
	
	public void move(int x, int y){
		if(!dungeon.checkBounds(x, y)) return;
		if(!Tile.tiles[dungeon.getTile(x, y)].isSolid()){
			//TODO: Seriously, fix this code. It breaks so much.
			if(!game.checkForEntity(x, y)){
				setX(x);
				setY(y);
			}else return;
		}else return;
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

}

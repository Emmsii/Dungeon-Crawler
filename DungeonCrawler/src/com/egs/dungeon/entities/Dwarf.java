package com.egs.dungeon.entities;

import java.awt.Color;
import java.awt.Graphics2D;

import com.egs.dungeon.Game;
import com.egs.dungeon.level.Dungeon;
import com.egs.dungeon.level.Room;

public class Dwarf extends Mob{

	protected Room settlement;
		
	public Dwarf(int id, int x, int y, int sight, String icon, Game game, Dungeon dungeon){
		super(id, x, y, sight, icon, game, dungeon);
	}
	
	public void update(){
		time++;
		if(time % 10 == 0) checkIfOnScreen();
		
		if(!onScreen) updateLite();
		else updateHeavy();
	}
	
	private void updateLite(){
		//TODO: Not very intensive changes here.
	}
	
	private void updateHeavy(){
		//TODO: Improve movement ai.
		//TODO: Should really implement line of sight...
		wanderSettlement(sight, settlement);
	}
	
	public void render(Graphics2D g){
		g.setColor(new Color(198, 232, 124));
		g.fillRect((x * game.tileSize) + game.xCam, (y * game.tileSize) + game.yCam, game.tileSize, game.tileSize);
		g.setColor(Color.WHITE);
		g.drawString(icon, (x * game.tileSize) + game.xCam, (y * game.tileSize) + game.yCam);
	}

	public Room getSettlement() {
		return settlement;
	}

	public void setSettlement(Room settlement) {
		this.settlement = settlement;
	}
}

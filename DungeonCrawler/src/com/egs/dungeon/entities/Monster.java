package com.egs.dungeon.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

import rlforj.los.IFovAlgorithm;
import rlforj.los.ShadowCasting;

import com.egs.dungeon.Game;
import com.egs.dungeon.level.Dungeon;
import com.egs.dungeon.util.Node;
import com.egs.dungeon.util.Vector2i;


public class Monster extends Mob{

	public List<Node> path;
	
	public Monster(int id, int x, int y, int sight, String icon, Game game, Dungeon dungeon) {
		super(id, x, y, sight, icon, game, dungeon);
	}
	
	public void update(){
		time++;
		checkIfOnScreen();
		
		if(!onScreen) updateLite();
		else updateHeavy();
	}
	
	private void updateLite(){
		
	}
	
	private void updateHeavy(){
//		wander(30);
		followMob(game.player, 30);
//		move();
	}
	
	public void render(Graphics2D g){
		g.setColor(Color.RED);
		g.fillRect((x * game.tileSize) + game.xCam, (y * game.tileSize) + game.yCam, game.tileSize, game.tileSize);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.BOLD, game.getTileSize()));
		g.drawString(icon, (x * game.tileSize) + game.xCam + 3, (y * game.tileSize) + game.yCam + 10);
	}
	
	public void move(){
		int px = game.player.getX();
		int py = game.player.getY();
		Vector2i start = new Vector2i(x, y);
		Vector2i end = new Vector2i(px, py);
		if(time % 10 == 0) path = game.pathFind(start, end, sight);
		
		if(path != null){
			if(path.size() > 0){
				Vector2i vec = path.get(path.size() - 1).getTile();
				if(x < vec.getX()) x++;
				if(x > vec.getX()) x--;
				if(y < vec.getY()) y++;
				if(y > vec.getY()) y--;
			}
		}
	}
	
	public interface ILosBoard{
		public boolean contains(int x, int y);
		public boolean isObsticle(int x, int y);
		public void visit(int x, int y);
		
		
	}
	

	
	
}

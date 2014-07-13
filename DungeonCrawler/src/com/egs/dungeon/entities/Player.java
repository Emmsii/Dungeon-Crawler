package com.egs.dungeon.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.egs.dungeon.Game;
import com.egs.dungeon.MainComponent;
import com.egs.dungeon.level.Dungeon;
import com.egs.dungeon.level.LOSBoard;
import com.egs.dungeon.util.InputHandler;

public class Player extends Mob{

	private LOSBoard board;
	private InputHandler input;
	private MainComponent main;
	
	public Player(int id, int x, int y, String icon, double karma, int sight, Game game, Dungeon dungeon, MainComponent main, InputHandler input, LOSBoard board){
		super(id, "player", icon, karma, sight, x, y, game, dungeon);
		this.board = board;
		this.input = input;
		this.main = main;
			
	}
	
	public void render(Graphics2D g){
		g.setColor(Color.YELLOW);
		g.fillRect((main.getWidth() / 2), (main.getHeight() / 2), game.getTileSize(), game.getTileSize());	
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, game.getTileSize()));
		g.drawString(icon, (main.getWidth() / 2) + 3, (main.getHeight() / 2) + 10);
	}
	
	public void update(){
		//Overrides Mob update method (hopefully)
		input();
	}
	
	private void input(){
		if(input.up.pressed){
			move(x, y - 1);
			board.visit(x, y - 1);
		}
		if(input.down.pressed){
			move(x, y + 1);
			board.visit(x, y + 1);
		}
		if(input.right.pressed){
			move(x + 1, y);
			board.visit(x + 1, y);
		}
		if(input.left.pressed){
			move(x - 1, y);
			board.visit(x + 1, y);
		}
		input.release();
	}
}

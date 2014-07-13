package com.egs.dungeon.entities;

import java.awt.Graphics2D;
import java.util.Random;

import com.egs.dungeon.Game;
import com.egs.dungeon.level.Dungeon;

public class Entity {
	
	protected Game game;
	protected Dungeon dungeon;
	protected Random random;
	
	protected int id;
	protected int x;
	protected int y;
	
	public Entity(int id, int x, int y, Game game, Dungeon dungeon){
		this.game = game;
		this.dungeon = dungeon;
		random = new Random();
		
		this.id = id;
		this.x = x;
		this.y = y;
	}
	
	public void render(Graphics2D g){
		
	}
	
	public void update(){
		
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public Dungeon getDungeon() {
		return dungeon;
	}

	public void setDungeon(Dungeon dungeon) {
		this.dungeon = dungeon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
}

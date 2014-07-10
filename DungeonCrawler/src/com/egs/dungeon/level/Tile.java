package com.egs.dungeon.level;

import java.awt.Color;
import java.awt.Graphics2D;

public class Tile {

	public static Tile[] tiles = new Tile[200];
	
	protected byte id;
	protected String name;
	protected boolean solid;
	protected boolean liquid;
	
	protected Color color;
	
	public Tile(int id, String name, boolean solid, boolean liquid, String color){
		if(tiles[id] != null){
			throw new RuntimeException("Duplicated tile id: " + id);
		}else{
			tiles[id] = this;
			this.id = (byte) id;
			this.name = name;
			this.solid = solid;
			this.liquid = liquid;
			this.color = stringToColor(color);
			System.out.println("Tile Registered: ID: " + id);
		}
	}
	
	public void render(Graphics2D g, int x, int y, int tileSize){
		g.setColor(color);
		g.fillRect(x, y, tileSize, tileSize);
	}
	
	private Color stringToColor(String data){
		String[] array = data.split(",");
		
		int r = Integer.parseInt(array[0]);
		int g = Integer.parseInt(array[1]);
		int b = Integer.parseInt(array[2]);
		
		return new Color(r, g, b);
		
	}
	
	public boolean isSolid(){
		return solid;
	}
	
	public Color getColor(){
		return color;
	}
}

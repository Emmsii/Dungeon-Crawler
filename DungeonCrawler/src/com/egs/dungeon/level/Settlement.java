package com.egs.dungeon.level;

public class Settlement extends Room{

	protected String type;
	
	public Settlement(int id, int x, int y, int width, int height, boolean isSpecial, boolean isSettlement) {
		super(id, x, y, width, height, isSpecial, isSettlement);
	}

}

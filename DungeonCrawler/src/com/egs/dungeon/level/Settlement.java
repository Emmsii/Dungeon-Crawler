package com.egs.dungeon.level;

public class Settlement extends Room{

	protected String type;
	protected int maxPop;
	
	public Settlement(int id, String type, int maxPop, int x, int y, int width, int height, boolean isSpecial, boolean isSettlement) {
		super(id, x, y, width, height, isSpecial, isSettlement);
		this.type = type;
		this.maxPop = maxPop;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMaxPop() {
		return maxPop;
	}

	public void setMaxPop(int maxPop) {
		this.maxPop = maxPop;
	}

}

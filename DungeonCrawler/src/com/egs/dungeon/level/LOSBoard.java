package com.egs.dungeon.level;

import rlforj.los.ILosBoard;

public class LOSBoard implements ILosBoard{

	protected int size;
	
	protected boolean[][] obsticles;
	protected boolean[][] visited; 
	
	public LOSBoard(int size){
		this.size = size;
		
		obsticles = new boolean[size][size];
		visited = new boolean[size][size];
	}
	
	public boolean contains(int x, int y) {
		return x >= 0 && y >= 0 && x < size && y < size;
	}

	public void setObsticle(int x, int y){
		obsticles[x][y] = true;
	}
	
	public boolean isObstacle(int x, int y) {
		return obsticles[x][y];
	}

	public void visit(int x, int y) {
		visited[x][y] = true;
	}
	
	public boolean getVisited(int x, int y){
		return visited[x][y];
	}
	

}

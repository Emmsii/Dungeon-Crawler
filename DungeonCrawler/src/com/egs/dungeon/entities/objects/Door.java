package com.egs.dungeon.entities.objects;

import com.egs.dungeon.Game;
import com.egs.dungeon.entities.Entity;
import com.egs.dungeon.level.Dungeon;

public class Door extends Entity{

	protected Door(int id, int x, int y, Game game, Dungeon dungeon) {
		super(id, x, y, game, dungeon);
	}

}

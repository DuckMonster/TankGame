package com.emilstrom.tanks.game;

import com.emilstrom.tanks.game.entity.Tank;
import com.emilstrom.tanks.game.tiles.TileHandler;

/**
 * Created by Emil on 2014-03-18.
 */
public class Map {
	Game game;

	TileHandler tileHandler;
	Tank player;

	public Map(Game g) {
		game = g;
		tileHandler = new TileHandler(game);
		player = new Tank(game);
	}

	public void logic() {
		tileHandler.logic();
		player.logic();
	}

	public void draw() {
		tileHandler.draw();
		player.draw();
	}


	public void loadAssets() {
		tileHandler.loadAssets();
		player.loadAssets();
	}
}

package com.emilstrom.tanks.game;

import com.emilstrom.tanks.game.entity.Tank;

/**
 * Created by Emil on 2014-03-18.
 */
public class Map {
	Game game;
	Tank player;

	public Map(Game g) {
		game = g;
		player = new Tank(game);
	}

	public void logic() {
		player.logic();
	}

	public void draw() {
		player.draw();
	}


	public void loadAssets() {
		player.loadAssets();
	}
}

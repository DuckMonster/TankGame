package com.emilstrom.tanks.game.tiles;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.game.Sprite;
import com.emilstrom.tanks.game.entity.Entity;
import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-19.
 */
public class Tile extends Entity {
	public static final float TILE_SIZE = 2;

	TileHandler tileHandler;

	Sprite tileSprite;
	Vertex position;
	int tileID;

	public Tile(TileHandler th, Vertex p, int id, Game g) {
		super(g);

		tileHandler = th;

		position = new Vertex(p);
		tileID = id;

		tileSprite = new Sprite(R.drawable.temp, new Vertex(-0.5f,-0.5f), false);
		tileSprite.setColor(1f, 1f, 1f, 1f);
	}

	public void logic() {
	}

	public void draw() {
		tileSprite.draw(position.times(TILE_SIZE), new Vertex(TILE_SIZE, TILE_SIZE), 0);
	}


	public void loadAssets() {
		tileSprite.loadAssets();
	}
}

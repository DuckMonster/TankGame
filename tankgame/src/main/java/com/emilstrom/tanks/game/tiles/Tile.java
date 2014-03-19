package com.emilstrom.tanks.game.tiles;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.game.Sprite;
import com.emilstrom.tanks.game.entity.Entity;
import com.emilstrom.tanks.helper.Art;
import com.emilstrom.tanks.helper.Color;
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

	float integrity;

	public Tile(TileHandler th, Vertex p, int id, Game g) {
		super(g);

		tileHandler = th;

		position = new Vertex(p);
		tileID = id;

		tileSprite = new Sprite(Art.temp, false);
		tileSprite.setColor(1f, 1f, 1f, 1f);

		integrity = 20f;
	}

	public boolean isDead() { return integrity <= 0; }
	public boolean collidesWith(Vertex v, Vertex size) {
		Vertex myPos = position.times(TILE_SIZE);

		return (v.x + size.x/2 > myPos.x - TILE_SIZE/2 && v.x <= myPos.x + TILE_SIZE/2 &&
				v.y + size.y/2 > myPos.y - TILE_SIZE/2 && v.y <= myPos.y + TILE_SIZE/2);
	}

	public float hit(float energy) {
		if (isDead()) return 0;

		float damage = Math.min(energy, integrity);
		integrity -= damage;
		if (integrity <= 0) integrity = 0;

		return damage;
	}

	public void logic() {
		if (isDead()) return;
	}

	public void draw() {
		if (isDead()) return;

		tileSprite.setColor(new Color(1f, 1f, 1f, integrity/20f));
		tileSprite.draw(position.times(TILE_SIZE), new Vertex(TILE_SIZE, TILE_SIZE), 0);
	}
}

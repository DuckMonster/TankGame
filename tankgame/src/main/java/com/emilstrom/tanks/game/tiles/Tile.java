package com.emilstrom.tanks.game.tiles;

import android.os.SystemClock;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.game.Sprite;
import com.emilstrom.tanks.game.Tileset;
import com.emilstrom.tanks.game.entity.Entity;
import com.emilstrom.tanks.helper.Art;
import com.emilstrom.tanks.helper.Color;
import com.emilstrom.tanks.helper.GameMath;
import com.emilstrom.tanks.helper.Timer;
import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-19.
 */
public class Tile extends Entity {
	public static final float TILE_SIZE = 2;

	TileHandler tileHandler;

	public Vertex position;

	Sprite tileSprite, tileHitSprite;
	Tileset crackSprite;
	int tileID;

	float integrity;
	float hitDamage;

	Timer hitTimer;

	public Tile(TileHandler th, Vertex p, int id, Game g) {
		super(g);

		tileHandler = th;

		position = new Vertex(p);
		tileID = id;

		tileSprite = new Sprite(Art.stone, false);
		tileHitSprite = new Sprite(Art.blank, false);
		crackSprite = new Tileset(Art.crackSet, false);

		integrity = 20f;

		hitTimer = new Timer(0.4f, true);
	}

	public boolean isDead() { return integrity <= 0; }
	public boolean collidesWith(Vertex v, Vertex size) {
		Vertex myPos = position.times(TILE_SIZE);

		return (v.x + size.x/2 > myPos.x - TILE_SIZE/2 && v.x - size.x/2 <= myPos.x + TILE_SIZE/2 &&
				v.y + size.y/2 > myPos.y - TILE_SIZE/2 && v.y - size.y/2 <= myPos.y + TILE_SIZE/2);
	}

	public float hit(float energy) {
		if (isDead()) return 0;

		float damage = Math.min(energy, integrity);
		hitDamage = damage/integrity;
		integrity -= damage;
		if (integrity <= 0) integrity = 0;

		hitTimer.reset();

		return damage;
	}

	public void logic() {
		hitTimer.logic();

		if (isDead()) return;
	}

	public void draw() {
		if (isDead()) return;

		tileSprite.setColor(new Color(1f, 1f, 1f, 1f));
		tileSprite.draw(position.times(TILE_SIZE), new Vertex(TILE_SIZE, TILE_SIZE), 0);

//		if (1f - integrity/20f > 0.1f) {
//			int tx = (int)Math.floor((1f-integrity/20)*5f);
//			crackSprite.setColor(new Color(0f, 0f, 0f, 0.7f));
//			crackSprite.draw(tx, 0, position.times(TILE_SIZE), new Vertex(TILE_SIZE, TILE_SIZE), 0);
//		}
	}

	public void drawAbove() {
		if (!hitTimer.isDone()) {
			tileHitSprite.setColor(new Color(1f, 0f, 0f, hitDamage * (1f - hitTimer.percentageDone())));

			Vertex randomPos = new Vertex((float)GameMath.getRndDouble(-0.1f, 0.1f), (float)GameMath.getRndDouble(-0.1f, 0.1f)).times(1f - hitTimer.percentageDone());
			tileHitSprite.draw(position.plus(randomPos).times(TILE_SIZE), new Vertex(TILE_SIZE, TILE_SIZE), 0);
		}
	}
}

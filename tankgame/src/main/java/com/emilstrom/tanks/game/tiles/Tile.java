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
	public static final float TILE_SIZE = 2f;
	public static final int TILE_STONE = 0,
			TILE_COPPER = 1;

	TileHandler tileHandler;

	public Vertex position;

	Sprite tileHitSprite;
	Tileset tilesetSprite;
	int tileID, tilesetX, tilesetY;

	float integrity, maxIntegrity;
	float hitDamage;

	Timer hitTimer;

	public Tile(TileHandler th, Vertex p, int id, Game g) {
		super(g);

		tileHandler = th;

		position = new Vertex(p);
		tileID = id;

		tilesetSprite = new Tileset(Art.tileset, false);
		tileHitSprite = new Sprite(Art.blank, false);

		hitTimer = new Timer(0.4f, true);

		getTileStats();
	}

	public void getTileStats() {
		switch(tileID) {
			case TILE_STONE:
				tilesetX = 1;
				tilesetY = 0;

				maxIntegrity = 20f;
				break;

			case TILE_COPPER:
				tilesetX = 2;
				tilesetY = 0;

				maxIntegrity = 60f;
				break;
		}

		integrity = maxIntegrity;
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

	public void draw(int drawOffset) {
		if (isDead()) return;

		tilesetSprite.setColor(new Color(1f, 1f, 1f, 1f));
		tilesetSprite.draw(tilesetX, tilesetY, position.plus(new Vertex(tileHandler.mapWidth*drawOffset, 0)).times(TILE_SIZE), new Vertex(TILE_SIZE+0.05f, TILE_SIZE+0.05f), 0);

//		if (1f - integrity/20f > 0.1f) {
//			int tx = (int)Math.floor((1f-integrity/20)*5f);
//			crackSprite.setColor(new Color(0f, 0f, 0f, 0.7f));
//			crackSprite.draw(tx, 0, position.times(TILE_SIZE), new Vertex(TILE_SIZE, TILE_SIZE), 0);
//		}
	}

	public void drawAbove(int drawOffset) {
		if (!hitTimer.isDone()) {
			tileHitSprite.setColor(new Color(1f, 0f, 0f, hitDamage * (1f - hitTimer.percentageDone())));

			Vertex randomPos = new Vertex((float)GameMath.getRndDouble(-0.1f, 0.1f), (float)GameMath.getRndDouble(-0.1f, 0.1f)).times(1f - hitTimer.percentageDone());
			tileHitSprite.draw(position.plus(new Vertex(tileHandler.mapWidth*drawOffset, 0)).plus(randomPos).times(TILE_SIZE), new Vertex(TILE_SIZE, TILE_SIZE), 0);
		}
	}
}

package com.emilstrom.tanks.game.tiles;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.game.Tileset;
import com.emilstrom.tanks.game.entity.Entity;
import com.emilstrom.tanks.helper.Art;
import com.emilstrom.tanks.helper.Color;
import com.emilstrom.tanks.helper.GameMath;
import com.emilstrom.tanks.helper.Timer;
import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-21.
 */
public class Oredrop extends Entity {
	Tile tileHost;

	int tilesetx, tilesety, tilesetOffset;
	float rotation;

	Vertex positionOffset;

	Tileset oreset, orefillset;
	Timer pickupTimer;

	public Oredrop(Tile host, Game g) {
		super(g);

		tileHost = host;
		oreset = new Tileset(Art.oreset, false);
		orefillset = new Tileset(Art.orefillset, false);

		getOreStats();
	}

	public Vertex getScreenPosition(int drawOffset) { return tileHost.position.plus(new Vertex(tileHost.tileHandler.mapWidth*drawOffset, 0)).plus(positionOffset).times(Tile.TILE_SIZE); }

	public void getOreStats() {
		switch(tileHost.tileID) {
			case Tile.TILE_COPPER:
				tilesetx = 0;
				tilesety = 0;
				break;
		}

		tilesetOffset = GameMath.getRndInt(0, 2);
		rotation = (float)GameMath.getRndDouble(0, 360);
		positionOffset = new Vertex((float)GameMath.getRndDouble(-0.4, 0.4), (float)GameMath.getRndDouble(-0.4, 0.4));
	}

	public void pickup() {
		pickupTimer = new Timer(0.8f, false);
	}

	public void logic() {
		if (pickupTimer != null) pickupTimer.logic();
		else {
			//Check for pickup range
			Vertex tankPos = new Vertex(game.map.player.position);
			tankPos.x = (float)GameMath.mod(tankPos.x, tileHost.tileHandler.mapWidth * Tile.TILE_SIZE);

			if (tankPos.minus(tileHost.position.times(Tile.TILE_SIZE)).getLength() <= 3f) pickup();
		}
	}

	public void draw(int drawOffset) {
		if (pickupTimer == null) {
			oreset.draw(tilesetx + tilesetOffset, tilesety,
					getScreenPosition(drawOffset),
					new Vertex(Tile.TILE_SIZE * 0.4f, Tile.TILE_SIZE * 0.4f), rotation);
		} else if (!pickupTimer.isDone()) {
			float floatValue = (float)(1 - Math.pow(Math.E, -pickupTimer.percentageDone() * 5f));

			Vertex floatPos = game.map.player.position.minus(getScreenPosition(drawOffset)).times(floatValue);

			orefillset.setColor(new Color(1f, 1f, 1f, 1f - pickupTimer.percentageDone()));
			orefillset.draw(tilesetx + tilesetOffset, tilesety,
					getScreenPosition(drawOffset).plus(floatPos),
					new Vertex(Tile.TILE_SIZE * 0.4f, Tile.TILE_SIZE * 0.4f), rotation);
		}
	}
}

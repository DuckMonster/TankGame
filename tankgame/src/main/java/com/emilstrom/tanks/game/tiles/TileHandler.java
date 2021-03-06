package com.emilstrom.tanks.game.tiles;

import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.game.Sprite;
import com.emilstrom.tanks.game.Tileset;
import com.emilstrom.tanks.helper.Art;
import com.emilstrom.tanks.helper.Color;
import com.emilstrom.tanks.helper.GameMath;
import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-19.
 */
public class TileHandler {
	Tileset groundSprite;
	Game currentGame;
	Tile tileMap[];
	int mapWidth, mapHeight;

	public TileHandler(Game g) {
		currentGame = g;

		groundSprite = new Tileset(Art.tileset, false);

		generateMap();
	}

	public Tile getTile(int x, int y) {
		x = (int)GameMath.mod(x, mapWidth);
		if (y < 0 || y >= mapHeight) return null;

		return tileMap[x + mapWidth * y];
	}

	public int getMapOffset(int x) {
		return (int)Math.floor((float)x / (float)mapWidth);
	}

	public Tile collidesWith(Vertex p, Vertex s) {
		Vertex tilePos = new Vertex(p);

		tilePos.x = (float)Math.floor(p.x / Tile.TILE_SIZE);
		tilePos.y = (float)Math.floor(p.y / Tile.TILE_SIZE);

		for(int xx=(int)tilePos.x - 3; xx<=tilePos.x + 3; xx++)
			for(int yy=(int)tilePos.y - 3; yy<=tilePos.y + 3; yy++) {
				Tile t = getTile(xx, yy);
				if (t == null || t.isDead()) continue;

				Vertex checkPos = new Vertex(p).plus(new Vertex(mapWidth * Tile.TILE_SIZE, 0).times(-getMapOffset(xx)));

				if (t.collidesWith(checkPos, s)) return t;
			}

		return null;
	}

	public void explosion(Vertex pos, float force, int radius) {
		Vertex mapPos = new Vertex(pos);

		mapPos.x = (float)Math.floor(mapPos.x / Tile.TILE_SIZE);
		mapPos.y = (float)Math.floor(mapPos.y / Tile.TILE_SIZE);

		for(int xx = (int)mapPos.x - radius; xx <= (int)mapPos.x + radius; xx++)
			for(int yy = (int)mapPos.y - radius; yy <= (int)mapPos.y + radius; yy++) {
				Tile t = getTile(xx, yy);
				if (t == null || t.isDead()) continue;

				float perc = 1f - (new Vertex(xx, yy).minus(mapPos).getLength() / 5);
				t.hit(force * perc);
			}
	}

	public void generateMap() {
		mapHeight = 200;
		mapWidth = 100;
		tileMap = new Tile[mapWidth * mapHeight];
		for(int xx=0; xx<mapWidth; xx++)
			for(int yy=0; yy<mapHeight; yy++) {
				if (GameMath.getChance(0.02f))
					createOreVein(xx, yy, Tile.TILE_COPPER, 0.4f + 0.05f * yy);

				if (tileMap[xx + mapWidth*yy] == null)
					tileMap[xx + mapWidth*yy] = new Tile(this, new Vertex(xx, yy), 0, currentGame);
			}
	}

	public void createOreVein(int x, int y, int type, float strength) {
		if (y < 0 || y >= mapHeight) return;

		x = (int)GameMath.mod(x, mapWidth);

		tileMap[x + mapWidth * y] = new Tile(this, new Vertex(x, y), type, currentGame);
		strength -= GameMath.getRndDouble(-0.1, 0.5);

		if (strength > 0f) {
			int nmbrOfSplits = GameMath.getRndInt(1, 2);

			for(int i=0; i<nmbrOfSplits; i++) {
				int splitDir = GameMath.getRndInt(0, 3);

				Tile t;

				switch(splitDir) {
					case 0:
						t = getTile(x + 1, y);
						if (t != null && t.tileID != Tile.TILE_STONE) break;

						createOreVein(x + 1, y, type, strength);
						break;

					case 1:
						t = getTile(x - 1, y);
						if (t != null && t.tileID != Tile.TILE_STONE) break;

						createOreVein(x - 1, y, type, strength);
						break;

					case 2:
						t = getTile(x, y + 1);
						if (t != null && t.tileID != Tile.TILE_STONE) break;

						createOreVein(x, y + 1, type, strength);
						break;

					case 3:
						t = getTile(x, y - 1);
						if (t != null && t.tileID != Tile.TILE_STONE) break;

						createOreVein(x, y - 1, type, strength);
						break;
				}
			}
		}
	}

	public void logic() {
		Vertex cameraPos = new Vertex(Game.worldCamera.position);

		cameraPos.x = (float)Math.floor(cameraPos.x / Tile.TILE_SIZE);
		cameraPos.y = (float)Math.floor(cameraPos.y / Tile.TILE_SIZE);

		int nmbrOfDraws = (int)Math.ceil(currentGame.gameWidth / Tile.TILE_SIZE)/2 + 1;

		for(int xx = (int)cameraPos.x - nmbrOfDraws; xx <= cameraPos.x + nmbrOfDraws; xx++)
			for(int yy = (int)cameraPos.y - nmbrOfDraws; yy <= cameraPos.y + nmbrOfDraws; yy++) {
				Tile t = getTile(xx, yy);
				if (t == null) continue;

				t.logic();
			}
	}

	public void draw() {
		Vertex cameraPos = new Vertex(Game.worldCamera.position);

		cameraPos.x = (float)Math.floor(cameraPos.x / Tile.TILE_SIZE);
		cameraPos.y = (float)Math.floor(cameraPos.y / Tile.TILE_SIZE);

		int nmbrOfDraws = (int)Math.ceil(currentGame.gameWidth / Tile.TILE_SIZE)/2 + 1;

		for(int xx = (int)cameraPos.x - nmbrOfDraws; xx <= cameraPos.x + nmbrOfDraws; xx++)
			for(int yy = (int)cameraPos.y - nmbrOfDraws; yy <= cameraPos.y + nmbrOfDraws; yy++) {
				Tile t = getTile(xx, yy);
				if (t == null) {
					drawGround(xx, yy);
					continue;
				}

				t.draw(getMapOffset(xx));
			}

		for(int xx = (int)cameraPos.x - nmbrOfDraws; xx <= cameraPos.x + nmbrOfDraws; xx++)
			for(int yy = (int)cameraPos.y - nmbrOfDraws; yy <= cameraPos.y + nmbrOfDraws; yy++) {
				Tile t = getTile(xx, yy);
				if (t == null) continue;

				t.drawAbove(getMapOffset(xx));
			}
	}

	public void drawGround(int x, int y) {
		groundSprite.setColor(new Color(1f, 1f, 1f, 1f));
		groundSprite.draw(0, 0, new Vertex(x,y).times(Tile.TILE_SIZE), new Vertex(Tile.TILE_SIZE, Tile.TILE_SIZE), 0);
	}
}

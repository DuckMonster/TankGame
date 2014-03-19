package com.emilstrom.tanks.game.tiles;

import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-19.
 */
public class TileHandler {
	Game currentGame;
	Tile tileMap[];
	int mapWidth, mapHeight;

	public TileHandler(Game g) {
		currentGame = g;
		generateMap();
	}

	public Tile collidesWith(Vertex p, Vertex s) {
		Vertex mapPos = new Vertex(p);

		mapPos.x = (float)Math.floor(mapPos.x / Tile.TILE_SIZE);
		mapPos.y = (float)Math.floor(mapPos.y / Tile.TILE_SIZE);

		for(int xx=(int)mapPos.x - 1; xx<=mapPos.x+1; xx++)
			for(int yy=(int)mapPos.y - 1; yy<=mapPos.y+1; yy++) {
				if (xx < 0 || yy < 0 || xx >= mapWidth || yy >= mapHeight) continue;

				Tile t = tileMap[xx + mapWidth * yy];

				if (!t.isDead() && t.collidesWith(p, s)) return tileMap[xx + mapWidth * yy];
			}

		return null;
	}

	public void explosion(Vertex pos, float force, int radius) {
		Vertex mapPos = new Vertex(pos);

		mapPos.x = (float)Math.floor(mapPos.x / Tile.TILE_SIZE);
		mapPos.y = (float)Math.floor(mapPos.y / Tile.TILE_SIZE);

		for(int xx = (int)mapPos.x - radius; xx <= (int)mapPos.x + radius; xx++)
			for(int yy = (int)mapPos.y - radius; yy <= (int)mapPos.y + radius; yy++) {
				if (xx < 0 || yy < 0 || xx >= mapWidth || yy >= mapHeight) continue;

				float perc = 1f - (new Vertex(xx, yy).minus(mapPos).getLength() / 5);
				tileMap[xx + mapWidth * yy].hit(force * perc);
			}
	}

	public void generateMap() {
		mapHeight = 20;
		mapWidth = 20;
		tileMap = new Tile[mapWidth * mapHeight];
		for(int xx=0; xx<mapWidth; xx++)
			for(int yy=0; yy<mapHeight; yy++) {
				tileMap[xx + mapWidth*yy] = new Tile(this, new Vertex(xx, yy), 0, currentGame);
			}
	}

	public void logic() {
		for(Tile t: tileMap) t.logic();
	}

	public void draw() {
		Vertex cameraPos = new Vertex(Game.worldCamera.position);

		cameraPos.x = (float)Math.floor(cameraPos.x / Tile.TILE_SIZE);
		cameraPos.y = (float)Math.floor(cameraPos.y / Tile.TILE_SIZE);

		for(int xx = (int)cameraPos.x - 6; xx <= cameraPos.x + 6; xx++)
			for(int yy = (int)cameraPos.y - 6; yy <= cameraPos.y + 6; yy++) {
				if (xx < 0 || yy < 0 || xx >= mapWidth || yy >= mapHeight) continue;

				tileMap[xx + mapWidth * yy].draw();
			}
	}



	public void loadAssets() {
		for(Tile t: tileMap) t.loadAssets();
	}
}

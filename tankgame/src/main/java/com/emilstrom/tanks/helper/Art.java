package com.emilstrom.tanks.helper;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.game.SpriteData;
import com.emilstrom.tanks.game.TilesetData;

/**
 * Created by Emil on 2014-03-19.
 */
public class Art {
	public static SpriteData tank = new SpriteData(R.drawable.tank),
			blank = new SpriteData(R.drawable.blank),
			triangle = new SpriteData(R.drawable.triangle),
			circle = new SpriteData(R.drawable.circle),
			ring = new SpriteData(R.drawable.ring),
			dirt = new SpriteData(R.drawable.dirt),
			stone = new SpriteData(R.drawable.stone),
			crack = new SpriteData(R.drawable.crack),
			temp = new SpriteData(R.drawable.temp);

	public static TilesetData crackSet = new TilesetData(R.drawable.crackset, 16, 16),
			tileset = new TilesetData(R.drawable.tileset, 16, 16);

	public static void loadAssets() {
		tank.loadAssets();
		blank.loadAssets();
		triangle.loadAssets();
		circle.loadAssets();
		ring.loadAssets();
		temp.loadAssets();
		dirt.loadAssets();
		stone.loadAssets();
		crack.loadAssets();

		crackSet.loadAssets();
		tileset.loadAssets();
	}
}
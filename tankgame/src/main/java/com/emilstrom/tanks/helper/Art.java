package com.emilstrom.tanks.helper;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.game.SpriteData;

/**
 * Created by Emil on 2014-03-19.
 */
public class Art {
	public static SpriteData tank = new SpriteData(R.drawable.tank),
			blank = new SpriteData(R.drawable.blank),
			triangle = new SpriteData(R.drawable.triangle),
			circle = new SpriteData(R.drawable.circle),
			ring = new SpriteData(R.drawable.ring),
			temp = new SpriteData(R.drawable.temp);

	public static void loadAssets() {
		tank.loadAssets();
		blank.loadAssets();
		triangle.loadAssets();
		circle.loadAssets();
		ring.loadAssets();
		temp.loadAssets();
	}
}

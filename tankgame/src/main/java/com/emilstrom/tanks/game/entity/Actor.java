package com.emilstrom.tanks.game.entity;

import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.game.Sprite;
import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-18.
 */
public class Actor extends Entity {
	public Vertex position;
	Sprite sprite;

	public Actor(Game g) {
		super(g);
	}

	public void logic() {
	}

	public void draw() {
		sprite.draw(position);
	}


	public void loadAssets() {
		sprite.loadAssets();
	}
}

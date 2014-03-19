package com.emilstrom.tanks.game.entity;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.game.Sprite;
import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-18.
 */
public class Bullet extends Entity {
	Vertex position;

	Vertex direction;
	float velocity;
	float energy;
	Sprite sprite;

	public Bullet(Vertex pos, Vertex dir, Game g) {
		super(g);
		sprite = new Sprite(R.drawable.blank, new Vertex(0,0), false);

		position = pos;
		direction = dir;
		velocity = 5f;
		energy = 100f;
	}

	public void update() {
		position.add(direction.times(velocity * Game.updateTime));
	}

	public void draw() {
		sprite.draw(position, new Vertex(0.4f, 1.5f), direction.getDirection());
	}
}

package com.emilstrom.tanks.game.entity;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.game.Sprite;
import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-18.
 */
public class Bullet extends Entity {
	static Sprite bulletSprite = new Sprite(R.drawable.blank, new Vertex(0,0), false);

	Vertex position;

	Vertex direction;
	float velocity;
	float energy;

	public Bullet(Vertex pos, Vertex dir, Game g) {
		super(g);

		position = new Vertex(pos);
		direction = new Vertex(dir);
		velocity = 5f;
		energy = 100f;
	}

	public void logic() {
		position.add(direction.times(velocity * Game.updateTime));
	}

	public void draw() {
		bulletSprite.draw(position, new Vertex(2f, 5f), direction.getDirection() - 90);
	}
}

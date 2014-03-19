package com.emilstrom.tanks.game.entity;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.game.Sprite;
import com.emilstrom.tanks.game.tiles.Tile;
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
		velocity = 90f;
		energy = 30f;
	}

	public boolean isDead() { return energy <= 0; }

	public void logic() {
		if (isDead()) return;

		int precision = 40;
		for(int i=0; i<precision; i++) {
			Vertex checkPosition = position.plus(direction.times(velocity * Game.updateTime * ((float)i / (float)precision)));
			Tile t = game.map.tileHandler.collidesWith(checkPosition, new Vertex(0.7f, 3f));
			if (t != null) {
				energy -= t.hit(energy);
			}

			if (isDead()) {
				blowUp(checkPosition);
				break;
			}
		}

		position.add(direction.times(velocity * Game.updateTime));
	}

	public void blowUp(Vertex pos) {
		game.map.tileHandler.explosion(pos, 5f, 1);
	}

	public void draw() {
		if (isDead()) return;

		bulletSprite.draw(position, new Vertex(0.7f, 3f), direction.getDirection() - 90);
	}
}

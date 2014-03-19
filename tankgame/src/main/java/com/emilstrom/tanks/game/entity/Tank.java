package com.emilstrom.tanks.game.entity;

import android.os.SystemClock;
import android.util.Log;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.TankActivity;
import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.game.Sprite;
import com.emilstrom.tanks.helper.Color;
import com.emilstrom.tanks.helper.GameMath;
import com.emilstrom.tanks.helper.Input;
import com.emilstrom.tanks.helper.InputHelper;
import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-18.
 */
public class Tank extends Actor {
	Vertex movementControlSize = new Vertex(5f, 5f);
	Vertex movementControlPosition = new Vertex(0f, 0f);
	Vertex shootButtonSize = new Vertex(3f, 3f);
	Vertex shootButtonPosition = new Vertex(0f, 0f);

	Sprite uiBox, triangle;
	Input input, oldInput;

	float rotation, velocity;
	final float maxVelocity = 8f, acceleration = 30f, friction = 40f, turnSpeed = 120f;

	boolean usingMovement = false;
	Vertex movementPosition;

	Bullet bulletList[] = new Bullet[5];
	int bulletn = 0;

	public Tank(Game g) {
		super(g);

		sprite = new Sprite(R.drawable.tank, new Vertex(0,0), false);
		uiBox = new Sprite(R.drawable.blank, new Vertex(-0.5f, -0.5f), true);
		triangle = new Sprite(R.drawable.triangle, new Vertex(0,0), true);
		position = new Vertex(0,0);
	}

	Vertex getDirectionVertex() { return new Vertex(-(float)Math.sin(rotation / 180f * Math.PI), (float)Math.cos(rotation / 180f * Math.PI)); }

	public void logic() {
		super.logic();

		input = InputHelper.getInput();
		if (oldInput == null) oldInput = input;

		if (input.pressed && !oldInput.pressed) {
			//Check movement input
			if (input.position.x >= movementControlPosition.x && input.position.x < movementControlPosition.x + movementControlSize.x &&
					input.position.y >= movementControlPosition.y && input.position.y < movementControlPosition.y + movementControlSize.y) {
				usingMovement = true;
			}

			//Check shooting input
			if (input.position.x >= shootButtonPosition.x && input.position.x < shootButtonPosition.x + shootButtonSize.x &&
					input.position.y >= shootButtonPosition.y && input.position.y < shootButtonPosition.y + shootButtonSize.y) {
				shoot();
			}
		}

		if (!input.pressed) usingMovement = false;

		movement();

		for(Bullet b : bulletList) if (b != null) b.logic();

		//Move camera
		Vertex cameraPosition = position.plus(getDirectionVertex().times(3f));
		Game.worldCamera.position.add(cameraPosition.minus(Game.worldCamera.position).times(Game.updateTime * 10f));
		Game.worldCamera.setRotation(rotation);

		oldInput = input;
	}

	public void movement() {
		//Move!
		if (usingMovement) {
			movementPosition = new Vertex(input.position.minus(movementControlPosition));

			if (movementPosition.x > movementControlSize.x) movementPosition.x = movementControlSize.x;
			if (movementPosition.x <= 0) movementPosition.x = 0;
			if (movementPosition.y > movementControlSize.y) movementPosition.y = movementControlSize.y;
			if (movementPosition.x <= 0) movementPosition.x = 0;

			float xPerc = movementPosition.x / movementControlSize.x * 2.0f - 1.0f,
					yPerc = movementPosition.y / movementControlSize.y * 2.0f - 1.0f;

			if (Math.abs(xPerc) > 0.2f)
				rotation += turnSpeed * -xPerc * Game.updateTime;

			if (Math.abs(yPerc) > 0.2f) {
				float acc = (acceleration + friction) * Game.updateTime;

				if (yPerc > 0f) {
					if (velocity + acc < maxVelocity*yPerc) velocity += acc;
				} else if (yPerc < 0f) {
					if (velocity + acc > maxVelocity*yPerc) velocity -= acc;
				}
			}
		}

		Vertex directionVertex = getDirectionVertex();

		position.add(directionVertex.times(velocity*Game.updateTime));

		//Friction
		float f = friction * Game.updateTime;

		if (velocity > 0) {
			velocity = Math.max(0, velocity - f);
		} else {
			velocity = Math.min(0, velocity + f);
		}
	}

	public void shoot() {
		Vertex directionVertex = getDirectionVertex();

		bulletList[bulletn] = new Bullet(position, directionVertex, game);
		bulletn = (bulletn + 1) % bulletList.length;
	}

	public void draw() {
		sprite.draw(position, new Vertex(3,3), rotation);
		uiBox.setColor(new Color(1f, 1f, 1f, 0.3f));
		uiBox.draw(movementControlPosition, movementControlSize, 0);
		uiBox.draw(shootButtonPosition, shootButtonSize, 0);

		if (usingMovement) {
			for(int i=0; i<4; i++) {
				float r = SystemClock.uptimeMillis() * 0.05f;
				float xx = (float)GameMath.lengthDirX(r + 90*i, 1f),
						yy = (float)GameMath.lengthDirY(r + 90*i, 1f);

				triangle.draw(movementControlPosition.x + movementPosition.x + xx, movementControlPosition.y + movementPosition.y + yy, new Vertex(0.4f,0.4f), r + 90 * i + 90);
			}
		}

		for(Bullet b : bulletList) if (b != null) b.draw();
	}


	public void loadAssets() {
		uiBox.loadAssets();
		triangle.loadAssets();
		movementControlPosition = new Vertex(10f - movementControlSize.x - 0.7f, -game.gameHeight/2 + 0.7f);
		shootButtonPosition = new Vertex(-10f + 0.7f, -game.gameHeight/2 + 1.4f);

		Bullet.bulletSprite.loadAssets();

		super.loadAssets();
	}
}

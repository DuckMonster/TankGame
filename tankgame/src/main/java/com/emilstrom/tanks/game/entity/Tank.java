package com.emilstrom.tanks.game.entity;

import android.os.SystemClock;
import android.util.Log;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.TankActivity;
import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.game.Sprite;
import com.emilstrom.tanks.game.tiles.Tile;
import com.emilstrom.tanks.helper.Art;
import com.emilstrom.tanks.helper.Color;
import com.emilstrom.tanks.helper.GameMath;
import com.emilstrom.tanks.helper.Input;
import com.emilstrom.tanks.helper.InputHelper;
import com.emilstrom.tanks.helper.Timer;
import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-18.
 */
public class Tank extends Actor {
	Sprite uiBox, triangle;

	Vertex movementControlSize = new Vertex(5f, 5f);
	Vertex movementControlPosition = new Vertex(0f, 0f);
	Vertex shootButtonSize = new Vertex(3f, 3f);
	Vertex shootButtonPosition = new Vertex(0f, 0f);


	Input input[], oldInput[];

	public float rotation, velocity;
	final float maxVelocity = 8f, acceleration = 30f, friction = 40f, turnSpeed = 120f;

	float fireSpeed = 2.5f;
	Timer fireTimer = new Timer(1f / fireSpeed, true);

	int movementTouchID = -1;
	Vertex movementPosition;

	Bullet bulletList[] = new Bullet[5];
	int bulletn = 0;

	public Tank(Game g) {
		super(g);

		sprite = new Sprite(Art.tank, false);
		uiBox = new Sprite(Art.blank, true);
		triangle = new Sprite(Art.triangle, true);
		position = new Vertex(-5,-5);
	}

	Vertex getDirectionVertex() { return new Vertex(-(float)Math.sin(rotation / 180f * Math.PI), (float)Math.cos(rotation / 180f * Math.PI)); }

	public void logic() {
		super.logic();

		input = InputHelper.getInput();
		if (oldInput == null) oldInput = input;

		fireTimer.logic();

		//INPUT
		for(int i=0; i<input.length; i++) {
			if (input[i].pressed) {
				final Vertex inPos = input[i].position;

				//Check movement input
				if (movementTouchID == -1 && inPos.x >= movementControlPosition.x && inPos.x < movementControlPosition.x + movementControlSize.x &&
						inPos.y >= movementControlPosition.y && inPos.y < movementControlPosition.y + movementControlSize.y) {
					movementTouchID = i;
				}

				//Check shooting input
				if (fireTimer.isDone() &&
						inPos.x >= shootButtonPosition.x && inPos.x < shootButtonPosition.x + shootButtonSize.x &&
						inPos.y >= shootButtonPosition.y && inPos.y < shootButtonPosition.y + shootButtonSize.y) {
					shoot();
				}
			}
		}

		if (movementTouchID != -1 && !input[movementTouchID].pressed) movementTouchID = -1;

		movement();
		updateCamera();

		for(Bullet b : bulletList) if (b != null) b.logic();

		oldInput = input;
	}

	public void movement() {
		//Friction
		float f = friction * Game.updateTime;

		if (velocity > 0) {
			velocity = Math.max(0, velocity - f);
		} else {
			velocity = Math.min(0, velocity + f);
		}

		//Move!
		if (movementTouchID != -1) {
			movementPosition = new Vertex(input[movementTouchID].position.minus(movementControlPosition));

			if (movementPosition.x > movementControlSize.x) movementPosition.x = movementControlSize.x;
			if (movementPosition.x <= 0) movementPosition.x = 0;
			if (movementPosition.y > movementControlSize.y) movementPosition.y = movementControlSize.y;
			if (movementPosition.x <= 0) movementPosition.x = 0;

			float xPerc = movementPosition.x / movementControlSize.x * 2.0f - 1.0f,
					yPerc = movementPosition.y / movementControlSize.y * 2.0f - 1.0f;

			if (Math.abs(xPerc) > 0.1f)
				rotation += turnSpeed * -xPerc * Game.updateTime;

			if (Math.abs(yPerc) > 0.2f) {
				float acc = (acceleration + friction) * Game.updateTime;

				if (yPerc > 0f && velocity < maxVelocity * yPerc) {
					velocity = Math.min(velocity + acc, maxVelocity * yPerc);
				}

				if (yPerc < 0f && velocity > maxVelocity * yPerc) {
					velocity = Math.max(velocity - acc, maxVelocity * yPerc);
				}
			}
		}

		Vertex directionVertex = getDirectionVertex(),
				movementVertex = directionVertex.times(velocity*Game.updateTime);

		Tile t = game.map.tileHandler.collidesWith(position.plus(new Vertex(movementVertex.x, 0f)), new Vertex(2f, 2f));
		if (t != null) movementVertex.x = 0;
		t = game.map.tileHandler.collidesWith(position.plus(new Vertex(0f, movementVertex.y)), new Vertex(2f, 2f));
		if (t != null) movementVertex.y = 0;

		position.add(movementVertex);
	}

	public void shoot() {
		Vertex directionVertex = getDirectionVertex();

		bulletList[bulletn] = new Bullet(position, directionVertex, game);
		bulletn = (bulletn + 1) % bulletList.length;

		velocity = -8f;

		fireTimer.reset();
	}

	public void draw() {
		sprite.setColor(new Color(1f, 1f, 1f, 1f));
		sprite.draw(position, new Vertex(3, 3), rotation);

		uiBox.setColor(new Color(1f, 1f, 1f, 0.3f));
		uiBox.draw(movementControlPosition.plus(movementControlSize.times(0.5f)), movementControlSize, 0);
		uiBox.draw(shootButtonPosition.plus(shootButtonSize.times(0.5f)), shootButtonSize, 0);

		if (movementTouchID != -1) {
			for(int i=0; i<4; i++) {
				float r = SystemClock.uptimeMillis() * 0.05f;
				float xx = (float)GameMath.lengthDirX(r + 90*i, 1f),
						yy = (float)GameMath.lengthDirY(r + 90*i, 1f);

				triangle.draw(movementControlPosition.x + movementPosition.x + xx, movementControlPosition.y + movementPosition.y + yy, new Vertex(0.4f,0.4f), r + 90 * i + 90);
			}
		}

		for(Bullet b : bulletList) if (b != null) b.draw();
	}


	public void screenChanged() {
		movementControlPosition = new Vertex(10f - movementControlSize.x - 0.7f, -Game.currentGame.gameHeight/2 + 0.7f);
		shootButtonPosition = new Vertex(-10f + 0.7f, -Game.currentGame.gameHeight/2 + 1.4f);
	}

	public void updateCamera() {
		Vertex cameraPosition = position.plus(getDirectionVertex().times(3f));
		Game.worldCamera.position = new Vertex(cameraPosition);
		Game.worldCamera.setRotation(rotation);
	}
}

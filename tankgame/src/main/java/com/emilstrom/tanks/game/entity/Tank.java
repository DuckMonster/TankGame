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

	Sprite uiBox, triangle;
	Input oldInput;

	float rotation, velocity;
	final float maxVelocity = 8f, acceleration = 90f, turnSpeed = 120f;

	boolean usingMovement = false;
	Vertex movementPosition;

	public Tank(Game g) {
		super(g);

		sprite = new Sprite(R.drawable.tank, new Vertex(0,0), false);
		uiBox = new Sprite(R.drawable.blank, new Vertex(-0.5f, -0.5f), true);
		triangle = new Sprite(R.drawable.triangle, new Vertex(0,0), true);
		position = new Vertex(0,0);
	}

	public void logic() {
		super.logic();

		Input in = InputHelper.getInput();
		if (oldInput == null) oldInput = in;

		if (in.pressed && !oldInput.pressed) {
			if (in.position.x >= movementControlPosition.x && in.position.x < movementControlPosition.x + movementControlSize.x &&
					in.position.y >= movementControlPosition.y && in.position.y < movementControlPosition.y + movementControlSize.y) {
				usingMovement = true;
			}
		}

		if (!in.pressed) usingMovement = false;

		//Move!
		if (usingMovement) {
			movementPosition = new Vertex(in.position.minus(movementControlPosition));

			if (movementPosition.x > movementControlSize.x) movementPosition.x = movementControlSize.x;
			if (movementPosition.x <= 0) movementPosition.x = 0;
			if (movementPosition.y > movementControlSize.y) movementPosition.y = movementControlSize.y;
			if (movementPosition.x <= 0) movementPosition.x = 0;

			float xPerc = movementPosition.x / movementControlSize.x * 2.0f - 1.0f,
					yPerc = movementPosition.y / movementControlSize.y * 2.0f - 1.0f;

			Log.v(TankActivity.TAG, Float.toString(xPerc) + "/" + Float.toString(yPerc));

			rotation += turnSpeed * -xPerc * Game.updateTime;

			if (yPerc > 0f) {
				if (velocity + acceleration*Game.updateTime < maxVelocity*yPerc) velocity += acceleration*Game.updateTime;
			} else if (yPerc < 0f) {
				if (velocity + acceleration*Game.updateTime > maxVelocity*yPerc) velocity -= acceleration*Game.updateTime;
			}

			Log.v(TankActivity.TAG, Float.toString(xPerc) + "/" + Float.toString(yPerc) + "   = " + Float.toString(velocity) + "/" + Float.toString(maxVelocity));
		}

		Vertex directionVertex = new Vertex(-(float)Math.sin(rotation / 180f * Math.PI), (float)Math.cos(rotation / 180f * Math.PI));

		position.add(directionVertex.times(velocity*Game.updateTime));

		velocity -= velocity * 5f * Game.updateTime;

		oldInput = in;
	}

	public void draw() {
		sprite.draw(position, new Vertex(3,3), rotation);
		uiBox.setColor(new Color(1f, 1f, 1f, 0.3f));
		uiBox.draw(movementControlPosition, movementControlSize, 0);

		if (usingMovement) {
			for(int i=0; i<4; i++) {
				float r = SystemClock.uptimeMillis() * 0.05f;
				float xx = (float)GameMath.lengthDirX(r + 90*i, 1f),
						yy = (float)GameMath.lengthDirY(r + 90*i, 1f);

				triangle.draw(movementControlPosition.x + movementPosition.x + xx, movementControlPosition.y + movementPosition.y + yy, new Vertex(0.4f,0.4f), r + 90 * i + 90);
			}
		}
	}


	public void loadAssets() {
		uiBox.loadAssets();
		triangle.loadAssets();
		movementControlPosition = new Vertex(10f - movementControlSize.x - 0.7f, -game.gameHeight/2 + 0.7f);

		super.loadAssets();
	}
}

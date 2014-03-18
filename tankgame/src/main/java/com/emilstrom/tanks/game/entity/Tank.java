package com.emilstrom.tanks.game.entity;

import android.util.Log;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.TankActivity;
import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.game.Sprite;
import com.emilstrom.tanks.helper.Color;
import com.emilstrom.tanks.helper.Input;
import com.emilstrom.tanks.helper.InputHelper;
import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-18.
 */
public class Tank extends Actor {
	Vertex movementControlSize = new Vertex(6f, 6f);
	Vertex movementControlPosition = new Vertex(10f - movementControlSize.x - 0.3f, -game.gameHeight/2 + 0.3f);

	Sprite uiBox;
	Input oldInput;

	float rotation;
	boolean usingMovement = false;
	Vertex movementPosition;

	public Tank(Game g) {
		super(g);

		sprite = new Sprite(R.drawable.tank, new Vertex(0,0), false);
		uiBox = new Sprite(R.drawable.blank, new Vertex(-0.5f, -0.5f), true);
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

		//Move!
		if (usingMovement) {
			movementPosition = new Vertex(in.position.minus(movementControlPosition));

			float xPerc = movementPosition.x / movementControlSize.x * 2.0f - 1.0f,
					yPerc = movementPosition.y / movementControlSize.y * 2.0f - 1.0f;

			if (xPerc > 1.0f) xPerc = 1.0f;
			if (xPerc < -1.0f) xPerc = -1.0f;
			if (yPerc > 1.0f) yPerc = 1.0f;
			if (yPerc < -1.0f) yPerc = -1.0f;

			rotation += 40 * -xPerc * Game.updateTime;
		}

		oldInput = in;
	}

	public void draw() {
		sprite.draw(position, new Vertex(3,3), rotation);
		uiBox.setColor(new Color(1f, 1f, 1f, 0.3f));
		uiBox.draw(movementControlPosition, movementControlSize, 0);
	}


	public void loadAssets() {
		uiBox.loadAssets();
		movementControlPosition = new Vertex(10f - movementControlSize.x - 0.3f, -game.gameHeight/2 + 0.3f);

		super.loadAssets();
	}
}

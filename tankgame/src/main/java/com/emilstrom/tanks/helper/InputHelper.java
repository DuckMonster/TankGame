package com.emilstrom.tanks.helper;

import android.opengl.Matrix;
import android.util.Log;

import com.emilstrom.tanks.TankActivity;
import com.emilstrom.tanks.game.Game;

/**
 * Created by Emil on 2014-02-19.
 */
public class InputHelper {
	static boolean pressed[] = new boolean[5];
	static float x[] = new float[5], y[] = new float[5];

	public static void setPressed(int id, boolean p) {
		if (id >= 5) return;

		pressed[id] = p;
	}
	public static void setPosition(int id, float xx, float yy) {
		if (id >= 5) return;

		x[id] = xx; y[id] = yy;
	}

	public static Input[] getInput() {
		Input ret[] = new Input[5];

		for(int i=0; i<5; i++) {
			float coords[];

			if (pressed[i]) {
				coords = getGameCoords(x[i], y[i]);
			} else {
				coords = new float[]{0, 0};
			}

			ret[i] = new Input(coords[0], coords[1], pressed[i]);
		}

		return ret;
	}

	public static float[] getGameCoords(float x, float y) {
		if (Game.uiCamera == null) {
			return new float[]{0, 0};
		}

		float coords[] = {x, y, 0f, 1f},
				invVP[] = new float[16];

		Matrix.invertM(invVP, 0, Game.currentGame.getViewProjection(Game.uiCamera), 0);

		Matrix.multiplyMV(coords, 0, invVP, 0, coords, 0);
		return new float[]{coords[0], coords[1]};
	}
}
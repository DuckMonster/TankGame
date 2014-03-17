package com.emilstrom.tanks.game;

import android.opengl.Matrix;

import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-17.
 */
public class Camera {
	Vertex position;

	public Camera() {
	}

	public float[] getView() {
		float view[] = new float[16];
		Matrix.setLookAtM(view, 0, position.x, position.y, 5f, position.x, position.y, 0f, 0f, 1f, 0f);
		return view;
	}
}

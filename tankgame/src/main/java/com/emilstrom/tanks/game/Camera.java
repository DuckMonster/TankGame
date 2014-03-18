package com.emilstrom.tanks.game;

import android.opengl.Matrix;

import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-17.
 */
public class Camera {
	Vertex position;
	float rotation;

	public Camera() {
		position = new Vertex(0f, 0f);
	}

	public void setRotation(float r) { rotation = r / 180f * (float)Math.PI; }

	public float[] getView() {
		float view[] = new float[16];
		Matrix.setLookAtM(view, 0, position.x, position.y, 5f, position.x, position.y, 0f, -(float)Math.sin(rotation), (float)Math.cos(rotation), 0f);
		return view;
	}
}

package com.emilstrom.tanks.game;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Emil on 2014-03-17.
 */
public class Game implements GLSurfaceView.Renderer {
	public static double updateTime;
	public static Game currentGame;

	public Game() {
		currentGame = this;
	}

	public void logic(double time) {
		updateTime = time;
	}

	public void draw() {
	}


	//GL STUFF
	float projection[] = new float[16];
	Camera camera;
	public float[] getViewProjection() {
		float viewProjection[] = new float[16];
		Matrix.multiplyMM(viewProjection, 0, projection, 0, camera.getView(), 0);
		return viewProjection;
	}

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
	}

	public void onDrawFrame(GL10 unused) {
		draw();
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		float w = (float)width / (float)height;
		w *= 10f;
		Matrix.orthoM(projection, 0, -w, w, -10f, 10f, 2f, 10f);
	}
}

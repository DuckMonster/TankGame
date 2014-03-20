package com.emilstrom.tanks.game;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.TankActivity;
import com.emilstrom.tanks.helper.ShaderHelper;
import com.emilstrom.tanks.helper.Vertex;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Emil on 2014-03-17.
 */
public class Game implements GLSurfaceView.Renderer {
	public static float updateTime;
	public static Game currentGame;
	public static Camera worldCamera, uiCamera;

	private double oldTime = 0;

	public Map map;

	public Game() {
		worldCamera = new Camera();
		uiCamera = new Camera();

		currentGame = this;
		map = new Map(this);
	}

	public void logic() {
		if (oldTime == 0) oldTime = SystemClock.uptimeMillis();

		double newTime = SystemClock.uptimeMillis();
		updateTime = (float)(newTime - oldTime) * 0.001f;
		oldTime = newTime;

		map.logic();
	}

	public void draw() {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_STENCIL_BUFFER_BIT);

		GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		GLES20.glEnable(GLES20.GL_BLEND);

		map.draw();
	}


	//GL STUFF
	float projection[] = new float[16];
	public float[] getViewProjection(Camera c) {
		float viewProjection[] = new float[16];
		Matrix.multiplyMM(viewProjection, 0, projection, 0, c.getView(), 0);
		return viewProjection;
	}


	public float gameHeight, gameWidth;

	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(0f, 0f, 0f, 1f);

		ShaderHelper.loadShader();
	}

	public void onDrawFrame(GL10 unused) {
		logic();
		draw();
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		float h = (float)height / (float)width;
		h *= 10f;
		Matrix.orthoM(projection, 0, -10f, 10f, -h, h, 2f, 10f);

		gameHeight = h*2;
		gameWidth = 20f;

		map.loadAssets();
	}
}

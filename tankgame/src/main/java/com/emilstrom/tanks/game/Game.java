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
	public static double updateTime;
	public static Game currentGame;
	public static Camera worldCamera, uiCamera;

	Sprite temp, temp2;

	public Game() {
		currentGame = this;
		temp = new Sprite(R.drawable.temp, new Vertex(0f, 0f), false);
		temp2 = new Sprite(R.drawable.temp, new Vertex(-0.5f, 0f), true);
	}

	public void logic(double time) {
		updateTime = time;
	}

	public void draw() {
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

		float r = (float)SystemClock.uptimeMillis() * 0.05f;
		worldCamera.setRotation(r);

		//temp.draw(new Vertex(0,0), new Vertex(5, 5), r);
		//temp.draw(new Vertex(5,0), new Vertex(5, 5), 0);
		temp2.draw(new Vertex(-5,0), new Vertex(5, 5), r);
	}


	//GL STUFF
	float projection[] = new float[16];
	public float[] getViewProjection(Camera c) {
		float viewProjection[] = new float[16];
		Matrix.multiplyMM(viewProjection, 0, projection, 0, c.getView(), 0);
		return viewProjection;
	}



	public void onSurfaceCreated(GL10 unused, EGLConfig config) {
		GLES20.glClearColor(1f, 0f, 0f, 1f);

		ShaderHelper.loadShader();

		//Load assets
		temp.loadAssets();
		temp2.loadAssets();
	}

	public void onDrawFrame(GL10 unused) {
		draw();
	}

	public void onSurfaceChanged(GL10 unused, int width, int height) {
		float w = (float)width / (float)height;
		w *= 10f;
		Matrix.orthoM(projection, 0, -w, w, -10f, 10f, 2f, 10f);
		worldCamera = new Camera();
		uiCamera = new Camera();
	}
}

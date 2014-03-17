package com.emilstrom.tanks;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.helper.InputHelper;

/**
 * Created by Emil on 2014-02-18.
 */
public class GameSurface extends GLSurfaceView implements Runnable {
	static Thread gameThread;
	static boolean running = false;
	Game game;

	public GameSurface(Context c) {
		super(c);

		setEGLContextClientVersion(2);

		game = new Game();
		setRenderer(game);

		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

		stop();
		start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float w = getWidth(), h = getHeight();

		float x = (e.getX() - w/2) / (w/2);
		float y = (e.getY() - h/2) / (-h/2);

		InputHelper.setPosition(x, y);

		switch(e.getAction()) {
			case MotionEvent.ACTION_DOWN:
				InputHelper.setPressed(true);
				break;

			case MotionEvent.ACTION_UP:
				InputHelper.setPressed(false);
				break;
		}

		return true;
	}

	public void start() {
		if (running) return;
		running = true;

		gameThread = new Thread(this);
		gameThread.start();
	}

	public void stop() {
		if (!running) return;
		running = false;
		try {
			Thread.sleep(20);
		} catch(Exception e) {
			Log.v(TankActivity.TAG, e.toString());
			Log.v(TankActivity.TAG, "Cant wait for thread. Got shit to do.");
		}
	}

	public synchronized void run() {
		double oldTime = SystemClock.uptimeMillis();

		while(running) {
			double newTime = SystemClock.uptimeMillis();
			double time = (newTime - oldTime) * 0.001;
			oldTime = newTime;

			game.logic(time);
			requestRender();

			try{Thread.sleep(8);} catch(Exception e) {}
		}

		stop();
	}
}

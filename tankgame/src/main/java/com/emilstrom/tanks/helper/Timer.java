package com.emilstrom.tanks.helper;

import com.emilstrom.tanks.game.Game;

/**
 * Created by Emil on 2014-03-19.
 */
public class Timer {
	float timer, timerMax;

	public Timer(float currentTimer, boolean startFinished) {
		this.timerMax = currentTimer;
		if (startFinished) timer = timerMax;
		else timer = 0;
	}

	public boolean isDone() { return timer >= timerMax; }
	public float percentageDone() { return Math.min(1f, timer / timerMax); }

	public void reset() {
		timer = 0;
	}

	public void reset(float max) {
		timerMax = max;
		reset();
	}

	public void logic() {
		if (isDone()) return;

		timer += Game.updateTime;
	}
}

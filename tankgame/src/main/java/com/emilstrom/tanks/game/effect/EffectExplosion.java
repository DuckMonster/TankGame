package com.emilstrom.tanks.game.effect;

import android.opengl.GLES20;

import com.emilstrom.tanks.R;
import com.emilstrom.tanks.game.Game;
import com.emilstrom.tanks.game.Sprite;
import com.emilstrom.tanks.game.entity.Entity;
import com.emilstrom.tanks.helper.Art;
import com.emilstrom.tanks.helper.Color;
import com.emilstrom.tanks.helper.GameMath;
import com.emilstrom.tanks.helper.Timer;
import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-19.
 */
public class EffectExplosion extends Entity {
	Sprite smokeCloudSprite;

	class SmokeCloud {
		Vertex position;
		Vertex velocity;
		float size;

		Timer smokeTimer;

		public SmokeCloud(Vertex pos) {
			position = new Vertex(pos);
			float xSpeed = (float)GameMath.getRndDouble(-5.0, 5.0),
					ySpeed = (float)GameMath.getRndDouble(-5.0, 5.0);

			velocity = new Vertex(xSpeed, ySpeed);
			size = (float)GameMath.getRndDouble(1.2, 3.5);

			smokeTimer = new Timer((float)GameMath.getRndDouble(1.4, 3.2), false);
		}

		public boolean isDead() {
			return (size <= 0 || smokeTimer.isDone());
		}

		public void logic() {
			if (isDead()) return;

			position.add(velocity.times(Game.updateTime));

			velocity.x -= velocity.x * 2f * Game.updateTime;
			velocity.y -= velocity.y * 2f * Game.updateTime;

			size -= 0.5f * Game.updateTime;
			smokeTimer.logic();
		}

		public void draw() {
			if (isDead()) return;

			smokeCloudSprite.setColor(new Color(1f, 1f, 1f, 1f - smokeTimer.percentageDone()));
			//smokeCloudSprite.draw(position, new Vertex(size, size), 0);
		}
	}

	SmokeCloud smokeList[];
	Sprite ringSprite;
	Timer ringTimer = new Timer(0.8f, true);

	Vertex triggerPosition;

	public EffectExplosion(Game g) {
		super(g);

		smokeCloudSprite = new Sprite(Art.circle, false);
		ringSprite = new Sprite(Art.ring, false);
	}

	public void trigger(Vertex v) {
		triggerPosition = new Vertex(v);

		smokeList = new SmokeCloud[6];
		for(int i=0; i<smokeList.length; i++)
			smokeList[i] = new SmokeCloud(v);

		ringTimer.reset();
	}

	public void logic() {
		ringTimer.logic();

		if (smokeList != null)
			for(SmokeCloud s : smokeList)
				if (s != null) s.logic();
	}

	public void draw() {
		if (smokeList != null)
			for(SmokeCloud s : smokeList)
				if (s != null) s.draw();

		if (!ringTimer.isDone()) {
			float ringSize = (float)(1 - Math.pow(Math.E, -ringTimer.percentageDone() * 5.0)) * 10f;
			ringSprite.setColor(new Color(1f, 1f, 1f, 1f - ringTimer.percentageDone()));
			ringSprite.draw(triggerPosition, new Vertex(ringSize, ringSize), 0);
		}
	}
}

package com.emilstrom.tanks.game;

import android.opengl.GLES20;

import com.emilstrom.tanks.helper.Color;
import com.emilstrom.tanks.helper.ShaderHelper;
import com.emilstrom.tanks.helper.Vertex;

/**
 * Created by Emil on 2014-03-20.
 */
public class Tileset extends Sprite {
	public Tileset(TilesetData data, boolean isUI) {
		super(data, isUI);
	}

	public void uploadData(int tilex, int tiley) {
		((TilesetData)spriteData).uploadData(tilex, tiley, spriteColor, spriteAlphaColor, getMVPMatrix());
	}

	public void draw(int tilex, int tiley) {
		spriteData.shaderProgram = ShaderHelper.shaderProgramTile;
		spriteData.loadAttributes();
		uploadData(tilex, tiley);
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}


	public void draw(int tilex, int tiley, Vertex pos) {
		draw(tilex, tiley);
	}
	public void draw(int tilex, int tiley, float x, float y) {
		draw(tilex, tiley, new Vertex(x, y));
	}

	public void draw(int tilex, int tiley, Vertex pos, Vertex scale, float r) {
		setPosition(pos);
		rotate(r);
		scale(scale);
		draw(tilex, tiley);
	}
	public void draw(int tilex, int tiley, float x, float y, Vertex scale, float r) {
		draw(tilex, tiley, new Vertex(x, y), scale, r);
	}
	public void draw(int tilex, int tiley, float x, float y, float scalex, float scaley, float r) {
		draw(tilex, tiley, new Vertex(x, y), new Vertex(scalex, scaley), r);
	}
}

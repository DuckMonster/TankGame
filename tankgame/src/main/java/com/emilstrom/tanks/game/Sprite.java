package com.emilstrom.tanks.game;

import android.opengl.GLES20;
import android.opengl.Matrix;

import com.emilstrom.tanks.helper.Color;
import com.emilstrom.tanks.helper.ShaderHelper;
import com.emilstrom.tanks.helper.TextureHelper;
import com.emilstrom.tanks.helper.Vertex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by Emil on 2014-03-18.
 */
public class Sprite {
	float modelMatrix[] = new float[16];
	Color spriteColor, spriteAlphaColor;
	boolean isUI;

	SpriteData spriteData;

	public Sprite(SpriteData data, boolean isUI) {
		spriteData = data;

		setColor(1f, 1f, 1f, 1f);
		setAlphaColor(1f, 0f, 1f);
		this.isUI = isUI;
	}

	public void resetMatrix() {
		Matrix.setIdentityM(modelMatrix, 0);
	}

	public void setColor(Color c) {
		spriteColor = new Color(c);
	}
	public void setColor(float r, float g, float b, float a) {
		setColor(new Color(r, g, b, a));
	}

	public void setAlphaColor(Color c) {
		spriteAlphaColor = new Color(c);
	}
	public void setAlphaColor(float r, float g, float b) {
		setAlphaColor(new Color(r, g, b));
	}

	public void translate(Vertex v) {
		Matrix.translateM(modelMatrix, 0, v.x, v.y, 0f);
	}
	public void translate(float x, float y) {
		Matrix.translateM(modelMatrix, 0, x, y, 0f);
	}
	public void setPosition(Vertex v) {
		resetMatrix();
		translate(v);
	}
	public void setPosition(float x, float y) {
		resetMatrix();
		translate(x, y);
	}

	public void rotate(float a) {
		Matrix.rotateM(modelMatrix, 0, a, 0f, 0f, 1f);
	}

	public void scale(Vertex v) {
		Matrix.scaleM(modelMatrix, 0, v.x, v.y, 0f);
	}
	public void scale(float x, float y) {
		Matrix.scaleM(modelMatrix, 0, x, y, 0f);
	}

	public float[] getMVPMatrix() {
		float mvp[] = new float[16];
		Matrix.multiplyMM(mvp, 0, Game.currentGame.getViewProjection(isUI ? Game.uiCamera : Game.worldCamera), 0, modelMatrix, 0);
		return mvp;
	}

	public void uploadData() {
		spriteData.uploadData(spriteColor, spriteAlphaColor, getMVPMatrix());
	}

	public void draw() {
		uploadData();
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}


	public void draw(Vertex pos) {
		GLES20.glUseProgram(ShaderHelper.shaderProgram2D);
		spriteData.loadAttributes();

		setPosition(pos);
		draw();
	}
	public void draw(float x, float y) {
		draw(new Vertex(x, y));
	}

	public void draw(Vertex pos, Vertex scale, float r) {
		GLES20.glUseProgram(ShaderHelper.shaderProgram2D);
		spriteData.loadAttributes();

		setPosition(pos);
		rotate(r);
		scale(scale);
		draw();
	}
	public void draw(float x, float y, Vertex scale, float r) {
		draw(new Vertex(x, y), scale, r);
	}
	public void draw(float x, float y, float scalex, float scaley, float r) {
		draw(new Vertex(x, y), new Vertex(scalex, scaley), r);
	}
}
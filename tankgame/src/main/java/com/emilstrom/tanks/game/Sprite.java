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
	Camera currentCamera;
	boolean isUI;

	public Sprite(int textureID, Vertex origin, boolean isUI) {
		this.textureID = textureID;
		vertexOrigin = new Vertex(origin);

		setColor(1f, 1f, 1f, 1f);
		setAlphaColor(1f, 0f, 1f);
		this.isUI = isUI;
	}

	public void bindVertexBuffer() {
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject);
	}

	public void resetMatrix() {
		Matrix.setIdentityM(modelMatrix, 0);
	}

	public void setCamera(Camera c) {
		currentCamera = c;
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
		Matrix.multiplyMM(mvp, 0, Game.currentGame.getViewProjection(currentCamera), 0, modelMatrix, 0);
		return mvp;
	}

	public void uploadData() {
		setCamera(isUI ? Game.uiCamera : Game.worldCamera);

		float mvpMatrix[] = getMVPMatrix();
		GLES20.glUniformMatrix4fv(u_mvp, 1, false, mvpMatrix, 0);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandler);
		GLES20.glUniform1i(u_texture, 0);

		GLES20.glUniform4f(u_color, spriteColor.r, spriteColor.g, spriteColor.b, spriteColor.a);
		GLES20.glUniform3f(u_alphaColor, spriteAlphaColor.r, spriteAlphaColor.g, spriteAlphaColor.b);
	}

	public void draw() {
		uploadData();
		GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
	}


	public void draw(Vertex pos) {
		GLES20.glUseProgram(ShaderHelper.shaderProgram2D);
		loadAttributes();

		setPosition(pos);
		draw();
	}
	public void draw(float x, float y) {
		draw(new Vertex(x, y));
	}

	public void draw(Vertex pos, Vertex scale, float r) {
		GLES20.glUseProgram(ShaderHelper.shaderProgram2D);
		loadAttributes();

		setPosition(pos);
		scale(scale);
		rotate(r);
		draw();
	}
	public void draw(float x, float y, Vertex scale, float r) {
		draw(new Vertex(x, y), scale, r);
	}
	public void draw(float x, float y, float scalex, float scaley, float r) {
		draw(new Vertex(x, y), new Vertex(scalex, scaley), r);
	}



	//GL ASSETS
	FloatBuffer vertexData;
	Vertex vertexOrigin;

	protected int textureID, textureHandler;
	protected int vertexBufferObject;
	protected int a_position, a_texturePosition;
	protected int u_color, u_mvp, u_texture, u_alphaColor;

	public void loadAssets() {
		setupVertexObject();
		loadTexture();
		genVertexData(vertexOrigin);
		loadAttributes();
	}

	public void setupVertexObject() {
		int buffers[] = new int[1];
		GLES20.glGenBuffers(1, buffers, 0);
		vertexBufferObject = buffers[0];
	}

	public void loadTexture() {
		textureHandler = TextureHelper.loadTexture(textureID);
	}

	public void genVertexData(Vertex origin) {
		float data[] = {
				-0.5f - origin.x, -0.5f - origin.y,		0f, 1f,
				0.5f - origin.x, -0.5f - origin.y,		1f, 1f,
				-0.5f - origin.x, 0.5f - origin.y,		0f, 0f,
				0.5f - origin.x, 0.5f - origin.y,		1f, 0f
		};

		vertexData = ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexData.put(data);
		vertexData.position(0);

		uploadVertexData();
	}

	public void uploadVertexData() {
		bindVertexBuffer();
		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexData.capacity() * 4, vertexData, GLES20.GL_STATIC_DRAW);
	}

	public void loadAttributes() {
		bindVertexBuffer();

		u_color = GLES20.glGetUniformLocation(ShaderHelper.shaderProgram2D, "u_color");
		u_alphaColor = GLES20.glGetUniformLocation(ShaderHelper.shaderProgram2D, "u_alphaColor");
		u_texture = GLES20.glGetUniformLocation(ShaderHelper.shaderProgram2D, "u_texture");
		u_mvp = GLES20.glGetUniformLocation(ShaderHelper.shaderProgram2D, "u_mvpMatrix");

		a_position = GLES20.glGetAttribLocation(ShaderHelper.shaderProgram2D, "a_vertexPosition");
		a_texturePosition = GLES20.glGetAttribLocation(ShaderHelper.shaderProgram2D, "a_texturePosition");

		GLES20.glEnableVertexAttribArray(a_position);
		GLES20.glEnableVertexAttribArray(a_texturePosition);

		GLES20.glVertexAttribPointer(a_position, 2, GLES20.GL_FLOAT, false, 4 * 4, 0);
		GLES20.glVertexAttribPointer(a_texturePosition, 2, GLES20.GL_FLOAT, false, 4 * 4, 2 * 4);
	}
}
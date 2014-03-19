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
public class SpriteData {
	public SpriteData(int textureID) {
		this.textureID = textureID;
	}

	public void bindVertexBuffer() {
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexBufferObject);
	}

	public void uploadData(Color color, Color alphaColor, float mvpMatrix[]) {
		GLES20.glUniformMatrix4fv(u_mvp, 1, false, mvpMatrix, 0);

		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandler);
		GLES20.glUniform1i(u_texture, 0);

		GLES20.glUniform4f(u_color, color.r, color.g, color.b, color.a);
		GLES20.glUniform3f(u_alphaColor, alphaColor.r, alphaColor.g, alphaColor.b);
	}

	//GL ASSETS
	FloatBuffer vertexData;

	protected int textureID, textureHandler;
	protected int vertexBufferObject;
	protected int a_position, a_texturePosition;
	protected int u_color, u_mvp, u_texture, u_alphaColor;

	public void loadAssets() {
		setupVertexObject();
		loadTexture();
		genVertexData();
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

	public void genVertexData() {
		float data[] = {
				-0.5f, -0.5f,		0f, 1f,
				0.5f, -0.5f,		1f, 1f,
				-0.5f, 0.5f,		0f, 0f,
				0.5f, 0.5f,			1f, 0f
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
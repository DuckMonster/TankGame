package com.emilstrom.tanks.game;

import android.opengl.GLES20;

import com.emilstrom.tanks.helper.Color;
import com.emilstrom.tanks.helper.ShaderHelper;
import com.emilstrom.tanks.helper.TextureHelper;
import com.emilstrom.tanks.helper.Vertex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by Emil on 2014-03-20.
 */
public class TilesetData extends SpriteData {
	Vertex textureSize, tileSize, tileSpacing;
	Color tileSpaceColor;
	int u_tilePosition, u_tileSize, u_tileSpacing, u_tileSpaceColor;

	public TilesetData(int textureID, int tileSizex, int tileSizey) {
		super(textureID);

		shaderProgram = ShaderHelper.shaderProgramTile;
		tileSize = new Vertex(tileSizex, tileSizey);
		tileSpaceColor = new Color(0f, 0f, 0f, 1f);
	}

	public void loadTexture() {
		int texSize[] = new int[2];
		textureHandler = TextureHelper.loadTexture(textureID, texSize);

		textureSize = new Vertex(texSize[0], texSize[1]);
		tileSpacing = new Vertex(1f / texSize[0], 1f / texSize[1]);
	}

	public void genVertexData() {
		tileSize.x = tileSize.x / textureSize.x;
		tileSize.y = tileSize.y / textureSize.y;

		float data[] = {
				-0.5f, -0.5f,		0f, tileSize.y,
				0.5f, -0.5f,		tileSize.x, tileSize.y,
				-0.5f, 0.5f,		0f, 0f,
				0.5f, 0.5f,			tileSize.x, 0f
		};

		vertexData = ByteBuffer.allocateDirect(data.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		vertexData.put(data);
		vertexData.position(0);

		uploadVertexData();
	}

	public void loadAttributes() {
		super.loadAttributes();

		u_tilePosition = GLES20.glGetUniformLocation(shaderProgram, "u_tilePosition");
		u_tileSize = GLES20.glGetUniformLocation(shaderProgram, "u_tileSize");
		u_tileSpacing = GLES20.glGetUniformLocation(shaderProgram, "u_tileSpacing");
		u_tileSpaceColor = GLES20.glGetUniformLocation(shaderProgram, "u_tileSpaceColor");
	}

	public void uploadData(int tilex, int tiley, Color color, Color alphaColor, float mvpMatrix[]) {
		super.uploadData(color, alphaColor, mvpMatrix);
		GLES20.glUniform2f(u_tilePosition, tilex, tiley);
		GLES20.glUniform2f(u_tileSize, tileSize.x, tileSize.y);
		GLES20.glUniform2f(u_tileSpacing, tileSpacing.x, tileSpacing.y);
		GLES20.glUniform3f(u_tileSpaceColor, tileSpaceColor.r, tileSpaceColor.g, tileSpaceColor.b);
	}
}

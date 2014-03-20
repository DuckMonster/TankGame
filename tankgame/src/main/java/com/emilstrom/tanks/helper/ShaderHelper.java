package com.emilstrom.tanks.helper;

import android.opengl.GLES20;
import android.util.Log;

import com.emilstrom.tanks.TankActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.emilstrom.tanks.R;

/**
 * Created by Emil on 2014-02-19.
 */
public class ShaderHelper {
	public static int shaderProgram2D, shaderProgramTile;
	public static void loadShader() {
		int vertexShader = ShaderHelper.createShader(GLES20.GL_VERTEX_SHADER, R.raw.vertex_shader),
				fragmentShader = ShaderHelper.createShader(GLES20.GL_FRAGMENT_SHADER, R.raw.fragment_shader),
				fragmentTileShader = ShaderHelper.createShader(GLES20.GL_FRAGMENT_SHADER, R.raw.tile_fragment_shader);

		shaderProgram2D = ShaderHelper.createAndLinkProgram(vertexShader, fragmentShader);
		shaderProgramTile = ShaderHelper.createAndLinkProgram(vertexShader, fragmentTileShader);
	}

	public static String readTextFile(int resourceID) {
		InputStream inputStream = TankActivity.context.getResources().openRawResource(resourceID);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

		String retString = "",
				nextLine;

		try {
			while((nextLine = bufferedReader.readLine()) != null) {
				retString += nextLine + '\n';
			}
		} catch(Exception e) {
			throw new RuntimeException("Fuck got shitted");
		}

		return retString;
	}

	public static int createShader(int shaderType, int sourceRawID) {
		int shader = GLES20.glCreateShader(shaderType);

		String source = readTextFile(sourceRawID);
		GLES20.glShaderSource(shader, source);
		GLES20.glCompileShader(shader);

		Log.v(TankActivity.TAG, "Shader debug: " + GLES20.glGetShaderInfoLog(shader));

		return shader;
	}

	public static int createAndLinkProgram(int vshader, int fshader) {
		int program = GLES20.glCreateProgram();
		GLES20.glAttachShader(program, vshader);
		GLES20.glAttachShader(program, fshader);

		GLES20.glLinkProgram(program);

		return program;
	}
}

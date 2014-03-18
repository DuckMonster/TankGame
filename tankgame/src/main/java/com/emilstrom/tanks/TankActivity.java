package com.emilstrom.tanks;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.FrameLayout;

public class TankActivity extends Activity {
	public static final String TAG = "TankGame";
	public static Context context;

	GameSurface surface;

	@Override
	protected void onCreate(Bundle currentState) {
		super.onCreate(currentState);

		setContentView(R.layout.activity_tank);

		context = this;
		surface = new GameSurface(this);

		FrameLayout frame = (FrameLayout)findViewById(R.id.container);
		frame.addView(surface);
	}
}
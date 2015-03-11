package com.example.requestingdata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class ChangeColor extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent data = new Intent();
		
		int color = getResources().getColor(R.color.red);
		
		data.putExtra("colorID", color);
		setResult(1, data);
		finish();
	}
}

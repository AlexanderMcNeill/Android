package com.bit.mcnear1.languagetrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SplashScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		
		Button btnStartQuiz = (Button)findViewById(R.id.btnStartQuiz);
		btnStartQuiz.setOnClickListener(new StartQuizBtnHandler());
	}

	
	public class StartQuizBtnHandler implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			Intent startQuizIntent = new Intent(SplashScreen.this, QuestionActivity.class);
			startActivity(startQuizIntent);
		}
		
	}
}

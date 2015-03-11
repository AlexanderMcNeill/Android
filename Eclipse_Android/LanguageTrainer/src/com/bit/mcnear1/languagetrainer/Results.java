package com.bit.mcnear1.languagetrainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Results extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
		
		Intent intent = getIntent();
		
		String score = intent.getStringExtra(QuestionActivity.SCORE);
		
		TextView txtScore = (TextView)findViewById(R.id.txtScore);
		txtScore.setText(score);
		
		Button againBtn = (Button)findViewById(R.id.btnAgain);
		againBtn.setOnClickListener(new AgainBtnHandler());
	}

	
    //Class that handles all of the events of the next button
	public class AgainBtnHandler implements OnClickListener
	{

		@Override
		public void onClick(View v) {
			//If the quiz is over moving through to the results activity
			Intent restartQuizIntent = new Intent(Results.this, QuestionActivity.class);
			startActivity(restartQuizIntent);
			
		}
		
	}
}

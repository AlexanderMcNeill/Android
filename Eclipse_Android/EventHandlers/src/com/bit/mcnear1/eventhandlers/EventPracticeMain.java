package com.bit.mcnear1.eventhandlers;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class EventPracticeMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_practice_main);
        
        //Displaying toast on the opening of the activity
        Toast testToast = Toast.makeText(this, "Hello world", Toast.LENGTH_LONG);
        testToast.show();
        
        //Linking the controls i made in xml to local objects
        Button testBtn = (Button)findViewById(R.id.testBtn);
        EditText inputTxt = (EditText)findViewById(R.id.inputTxt);
        
        //Linking the buttons click listeners to the classes that will manage it
        testBtn.setOnClickListener(new TestButtonEvents());
        testBtn.setOnLongClickListener(new TestButtonEvents());
        
        //Linking the edit texts key listener to the class that will manage it
        inputTxt.setOnKeyListener(new TxtInputEvents());
        
    }

    public class TestButtonEvents implements OnClickListener, OnLongClickListener
    {

		@Override
		public void onClick(View v) {
			Toast statusToast = Toast.makeText(EventPracticeMain.this, "Hi there, normal click", Toast.LENGTH_LONG);
			statusToast.show();
		}

		@Override
		public boolean onLongClick(View v) {
			Toast statusToast = Toast.makeText(EventPracticeMain.this, "Hi there, long click", Toast.LENGTH_LONG);
			statusToast.show();
			return true;
		}
    	
    }
    
    public class TxtInputEvents implements OnKeyListener
    {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			//Checking if the user has tried to press the @ key and if the event is a release so the event doesn't run twice
			if(keyCode == KeyEvent.KEYCODE_AT && event.getAction() != KeyEvent.ACTION_UP)
			{
				Toast statusToast = Toast.makeText(EventPracticeMain.this, "Don't type '@'", Toast.LENGTH_LONG);
				statusToast.show();
				return true;
			}
			return false;
		}
    	
    }
}

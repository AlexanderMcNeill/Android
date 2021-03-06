package com.example.requestingdata;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

	protected final int  NEWCOLORREQUEST = 1;
	protected TextView txtFiller;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        txtFiller = (TextView)findViewById(R.id.txtFiller);
        
        txtFiller.setText("Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.");
    
        Button btnChangeTxt = (Button)findViewById(R.id.btnChangeTxt);
        btnChangeTxt.setOnClickListener(new ChangeTxtBtnHandler());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch(requestCode)
		{
			case NEWCOLORREQUEST:
				int colorID = data.getIntExtra("colorID", 0);
				
				if(colorID != 0)
				{
					txtFiller.setTextColor(colorID);
				}
				break;
		}
	}
    
    public class ChangeTxtBtnHandler implements OnClickListener
    {

		@Override
		public void onClick(View v) {
			Intent getNewColorIntent = new Intent(MainActivity.this, ChangeColor.class);
			startActivityForResult(getNewColorIntent, NEWCOLORREQUEST);
			
		}
    	
    }
}

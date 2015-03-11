package com.bit.mcnear1.eventhandlerst2;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.Toast;


public class UserNameMain extends Activity {

	private EditText userNameInput; 
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name_main);
        
        userNameInput = (EditText)findViewById(R.id.userNameTxt);
        
        userNameInput.setOnKeyListener(new UserNameFieldEvents());
    }
    
    public class UserNameFieldEvents implements OnKeyListener
    {

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_ENTER )
			{
				if(event.getAction() == KeyEvent.ACTION_UP)
				{
					if(userNameInput.getText().length() > 7)
					{
						Toast outputToast = Toast.makeText(UserNameMain.this, "Thank you " + userNameInput.getText(), Toast.LENGTH_LONG);
						outputToast.show();
					}
					else
					{
						Toast outputToast = Toast.makeText(UserNameMain.this, "Usernames must be 8 characters or more", Toast.LENGTH_LONG);
						outputToast.show();
					}
				}
				return true;
			}
			return false;
		}
    	
    }
}

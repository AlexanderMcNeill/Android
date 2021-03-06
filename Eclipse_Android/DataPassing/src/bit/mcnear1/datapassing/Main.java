package bit.mcnear1.datapassing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class Main extends Activity {

	TextView txtUsername;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button btnSettings = (Button)findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new SettingsBtnHandler());
        
        txtUsername = (TextView)findViewById(R.id.txtUsername);
        
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        
        if(username == null)
        {
        	txtUsername.setText("No username chosen yet...");
        }
        else
        {
        	txtUsername.setText(username);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public class SettingsBtnHandler implements OnClickListener
    {

		@Override
		public void onClick(View v) {
			Intent settingsIntent = new Intent(Main.this, Settings.class);
			startActivity(settingsIntent);
		}
    	
    }
}

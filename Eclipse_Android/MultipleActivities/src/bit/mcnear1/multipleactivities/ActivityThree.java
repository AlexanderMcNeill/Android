package bit.mcnear1.multipleactivities;

import bit.mcnear1.multipleactivities.ActivityOne.ChangeActivityBtnHandler;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityThree extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_one);
		
		TextView nameLbl = (TextView)findViewById(R.id.activityName);
	    nameLbl.setText("Activity Three");
	    
	    Button changeActivityBtn = (Button)findViewById(R.id.button1);
        changeActivityBtn.setOnClickListener(new ChangeActivityBtnHandler());
	}

	
    public class ChangeActivityBtnHandler implements OnClickListener
    {

		@Override
		public void onClick(View v) {
			Uri website = Uri.parse("http://www.google.com");
			Intent changeActivityIntent = new Intent(Intent.ACTION_VIEW, website);
			startActivity(changeActivityIntent);
		}
    	
    }
}

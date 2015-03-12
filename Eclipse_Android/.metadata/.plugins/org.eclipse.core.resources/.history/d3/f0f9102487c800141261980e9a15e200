package bit.mcnear1.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tablet_activity_main);
        
        Button btnList = (Button)findViewById(R.id.btnList);
        btnList.setOnClickListener(new ListBtnHandler());
        
        Button btnImage = (Button)findViewById(R.id.btnImage);
        btnImage.setOnClickListener(new ImageBtnHandler());
        
    }

    public class ImageBtnHandler implements OnClickListener
    {

		@Override
		public void onClick(View v) {
			Fragment imageFragment = new ImageFragment();
			FragmentManager fm = getFragmentManager();
			
			FragmentTransaction ft = fm.beginTransaction();
			
			ft.replace(R.id.imageFragmentContainer, imageFragment);
			
			ft.commit();
			
		}
    	
    }
    
    public class ListBtnHandler implements OnClickListener
    {

		@Override
		public void onClick(View v) {
			Fragment listFragment = new ListFragment();
			FragmentManager fm = getFragmentManager();
			
			FragmentTransaction ft = fm.beginTransaction();
			
			ft.replace(R.id.listFragmentContainer, listFragment);
			
			ft.commit();
		}
    	
    }
}

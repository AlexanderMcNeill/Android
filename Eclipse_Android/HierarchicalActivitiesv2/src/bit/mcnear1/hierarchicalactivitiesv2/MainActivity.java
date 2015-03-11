package bit.mcnear1.hierarchicalactivitiesv2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		ListView navList = (ListView)findViewById(R.id.left_drawer);
		NavListClickHandler navHandler = new NavListClickHandler();
		String[] pageNames = navHandler.getPageNames();
		
		ArrayAdapter<String> pageNameAdapter = new ArrayAdapter<String>(this,  android.R.layout.simple_list_item_1, pageNames);
		navList.setAdapter(pageNameAdapter);
		
		navList.setOnItemClickListener(navHandler);
    }
    
    
	public class NavListClickHandler implements OnItemClickListener {
		
		private String[] pages = {"Home", "Services", "Activities", "Dining", "Shopping"};
		private int[] imageIDs = {0, R.drawable.services, R.drawable.activities, R.drawable.dining, R.drawable.shopping};
		
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent goToIntent;
			String selectedPage = pages[position];
			
			if(selectedPage.equals("Home"))
			{
				goToIntent = new Intent(MainActivity.this, MainActivity.class);
			}
			else
			{
				goToIntent = new Intent(MainActivity.this, Child.class);
				goToIntent.putExtra("pageTitle", pages[position]);
				goToIntent.putExtra("imageID", imageIDs[position]);
			}
			
			startActivity(goToIntent);
		}
		
		public String[] getPageNames()
		{
			return pages;
		}

	}
}

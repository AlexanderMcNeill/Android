package bit.mcnear1_using_json_data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity {

	private List<Event> dunedinEvents;
	private ListView eventListView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        JSONObject dunedinEventsJson = jsonFromFile("dunedin_events.json");
        dunedinEvents = getEvents(dunedinEventsJson);
        
        String[] eventNames = new String[dunedinEvents.size()];
        
        for(int i = 0 ; i < eventNames.length; i++)
        {
        	eventNames[i] = dunedinEvents.get(i).getName();
        }
        
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, eventNames);
        
        eventListView = (ListView)findViewById(R.id.listEvents);
        eventListView.setAdapter(arrayAdapter);
        
        eventListView.setOnItemClickListener(new EventItemHandler());
    }

    private JSONObject jsonFromFile(String fileName)
    {
    	JSONObject output = null;
    	try {
	    	AssetManager am = getAssets();
	    	InputStream inputStream = am.open(fileName);
	    	int fileSizeInBytes = inputStream.available();
	    	byte[] jsonBuffer = new byte[fileSizeInBytes];
	    	
	    	inputStream.read(jsonBuffer);
	    	inputStream.close();
	    	
	    	String jsonString = new String(jsonBuffer);
	    	output = new JSONObject(jsonString);
    	}
    	catch(IOException ex) {
    		ex.printStackTrace();
    	}
    	catch (JSONException ex) {
			ex.printStackTrace();
		}
    	
    	return output;
    }
    
    private List<Event> getEvents(JSONObject eventsJson) {
		List<Event> events = new ArrayList<Event>();
		
		try{
			JSONArray jsonEventsArray = eventsJson.getJSONObject("events").getJSONArray("event");
			
			for(int i = 0; i < jsonEventsArray.length(); i++)
			{
				JSONObject jsonEvent = jsonEventsArray.getJSONObject(i);
				String eventName = jsonEvent.getString("title");
				String eventDescription = jsonEvent.getString("description");
				events.add(new Event(eventName, eventDescription));
			}
		}
		catch(JSONException ex){
			ex.printStackTrace();
		}
		return events;
	}
    
    public class EventItemHandler implements OnItemClickListener
    {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			
			Event currentItem = dunedinEvents.get(position);
			String currentItemDescription = currentItem.getDescription();
			
			Toast descriptionToast = Toast.makeText(MainActivity.this,currentItemDescription, Toast.LENGTH_LONG);
			descriptionToast.show();
			
		}
    	
    }
}

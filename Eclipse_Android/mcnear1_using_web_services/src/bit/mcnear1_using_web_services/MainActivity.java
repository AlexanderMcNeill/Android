package bit.mcnear1_using_web_services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {

	private final String baseURL = "http://ws.audioscrobbler.com/2.0/?";
	private final String apiSettings = "&api_key=58384a2141a4b9737eacb9d0989b8a8c&limit=10&format=json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UpdateList updateList = new UpdateList();
        updateList.execute(baseURL + "method=chart.getTopArtists" + apiSettings);
    }
    
    public class UpdateList extends AsyncTask<String, Void, String>
    {

		@Override
		protected String doInBackground(String... urlString) {
			String output = null;
			
			try {
				URL urlObject = new URL(urlString[0]);
				HttpURLConnection connection = (HttpURLConnection)urlObject.openConnection();
				connection.connect();
				
				int responseCode = connection.getResponseCode();
				
				if(responseCode != 200)
				{
					throw(new MalformedURLException());
				}
				
				InputStream is = connection.getInputStream();
				BufferedReader r = new BufferedReader(new InputStreamReader(is));
				
				StringBuilder sb = new StringBuilder();
				String line ="";
				
				while((line = r.readLine()) != null)
				{
					sb.append(line);
				}
				
				output = sb.toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
			
			return output;
		}
    	
		@Override
		protected void onPostExecute(String s){
			try {
				JSONObject output = new JSONObject(s);
				
				JSONArray artistJsons = output.getJSONObject("artists").getJSONArray("artist");
				
				Artist[] artists = new Artist[artistJsons.length()];
				for(int i =0; i < artists.length; i++)
				{
					JSONObject a = artistJsons.getJSONObject(i);
					artists[i] = new Artist(a.getString("name"), a.getInt("playcount"), null);
				}
				
				ArtistArrayAdapter artistAdapter = new ArtistArrayAdapter(MainActivity.this, R.layout.artist_list_item, artists);
				
				ListView artistList = (ListView)findViewById(R.id.listTopArtists);
				
				artistList.setAdapter(artistAdapter);
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		
		 public class ArtistArrayAdapter extends ArrayAdapter<Artist>
		 {

			public ArtistArrayAdapter(Context context, int resource,
					Artist[] objects) {
				super(context, resource, objects);
			}

			@Override
			public View getView(int position, View convertView, ViewGroup container) 
			{
				LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
				
				View v = inflater.inflate(R.layout.artist_list_item, container, false);

				TextView txtArtistName = (TextView)v.findViewById(R.id.artistName);
				TextView txtViewCount = (TextView)v.findViewById(R.id.playCount);
				
				// Get the current ToDo instance. Use the Adapter base class's getItem command
				Artist artist = getItem(position);
				
				// Use the data fields of the current ToDo instance to initialise the View controls correctly
				txtArtistName.setText(artist.getName());
				int playCount = artist.getPlayCount();
				//txtViewCount.setText(playCount.toString());
				
				
				// Return your customview
				return v;
			}
		 }
    }
}

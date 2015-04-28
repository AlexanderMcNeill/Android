package bit.mcnear1.teleportgamev2;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends Activity {

    private final String URLBASE = "http://www.geoplugin.net/extras/location.gp?";
    private final String URLDATAFORMAT = "&format=json";
    private final long LOCATIONUPDATETIME = 30000;
    private final float LOCATIONUPDATEDISTANCE = 4;

    private double longitude;
    private double latitude;

    protected TextView txtLatitude;
    protected TextView txtLongitude;
    protected TextView txtClosestCity;

    protected LocationManager locationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Method that links all the xml objects to their java counter parts
        linkXMLToObjects();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria locationCrit = new Criteria();

        String provider = locationManager.getBestProvider(locationCrit, false);

        locationManager.requestLocationUpdates(provider, 10000, LOCATIONUPDATEDISTANCE, new LocationUpdateHandler());
    }



    private void linkXMLToObjects()
    {
        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtClosestCity = (TextView) findViewById(R.id.txtClosestCity);
    }

    public class LocationUpdateHandler implements LocationListener
    {

        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            txtLatitude.setText(String.valueOf(latitude));
            txtLongitude.setText(String.valueOf(longitude));

            //GetGeoData getGeoData = new GetGeoData();
            //getGeoData.execute();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    }

    public class GetGeoData extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... params) {
            String urlString = URLBASE + "lat=" + latitude + "&long=" + longitude + URLDATAFORMAT;
            String output = null;

            try {
                URL urlObject = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection)urlObject.openConnection();
                connection.connect();

                int responseCode = connection.getResponseCode();

                if(responseCode == 200)
                {
                    InputStream is = connection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));

                    StringBuilder sb = new StringBuilder();

                    String line = "";

                    while ((line = br.readLine()) != null)
                    {
                        sb.append(line);
                    }

                    output = sb.toString();
                }

            }catch (MalformedURLException ex)
            {
                ex.printStackTrace();
            }
            catch (IOException ex)
            {
                ex.printStackTrace();
            }

            return output;
        }

        @Override
        protected void onPostExecute(String fetchedData)
        {
            try{
                JSONObject fetchedJson = new JSONObject(fetchedData);

                txtClosestCity.setText(fetchedJson.getString("geoplugin_place"));
            }catch (Exception ex)
            {
                txtClosestCity.setText("Unable to find close city");
            }
        }
    }
}

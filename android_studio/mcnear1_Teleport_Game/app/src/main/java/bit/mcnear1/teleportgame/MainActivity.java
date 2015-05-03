package bit.mcnear1.teleportgame;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;


public class MainActivity extends Activity {

    private final String URLBASE = "http://www.geoplugin.net/extras/location.gp?";
    private final String URLDATAFORMAT = "&format=json";
    private final long LOCATIONUPDATETIME = 10000;
    private final float LOCATIONUPDATEDISTANCE = 4;

    private double latitude = 0;
    private double longitude = 0;

    private TextView txtLatitude;
    private TextView txtLongitude;
    private TextView txtClosestCity;

    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtClosestCity = (TextView) findViewById(R.id.txtClosestCity);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria locationCrit = new Criteria();

        String provider = locationManager.getBestProvider(locationCrit, false);

        locationManager.requestLocationUpdates(provider, LOCATIONUPDATETIME, LOCATIONUPDATEDISTANCE, new LocationUpdateHandler());
    }


    public class LocationUpdateHandler implements LocationListener
    {

        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            txtLatitude.setText(String.valueOf(latitude));
            txtLongitude.setText(String.valueOf(longitude));

            String urlString = URLBASE + "lat=" + latitude + "&long=" + longitude + URLDATAFORMAT;

            GetGeoData getGeoData = new GetGeoData();
            getGeoData.execute(urlString);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {}

        @Override
        public void onProviderEnabled(String s) {}

        @Override
        public void onProviderDisabled(String s) {}
    }

    public class GetGeoData extends WebDataFetcher
    {

        @Override
        protected void processData(byte[] fetchedData)
        {
            try{
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);

                txtClosestCity.setText(fetchedJson.getString("geoplugin_place"));
            }catch (Exception ex)
            {
                txtClosestCity.setText("Unable to find close city");
            }
        }


    }
}

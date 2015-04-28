package bit.mcnear1.teleportgame;

import android.app.Activity;
import android.graphics.BitmapFactory;
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

    private final double MAXLATITUDE = 90;
    private final double MAXLONGITUDE = 180;
    private final String URLBASE = "http://www.geoplugin.net/extras/location.gp?";
    private final String URLDATAFORMAT = "&format=json";

    private double latitude = 0;
    private double longitude = 0;
    private Random rGen = new Random();

    private TextView txtLatitude;
    private TextView txtLongitude;
    private TextView txtClosestCity;
    private Button btnTeleport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        txtClosestCity = (TextView) findViewById(R.id.txtClosestCity);
        btnTeleport = (Button) findViewById(R.id.btnTeleport);

        btnTeleport.setOnClickListener(new btnTeleportHandler());
    }

    public class btnTeleportHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View v) {
            latitude = rGen.nextDouble();
            latitude *= MAXLATITUDE;

            if(rGen.nextInt(3) == 1)
            {
                latitude = -latitude;
            }

            txtLatitude.setText(String.valueOf(latitude));

            longitude = rGen.nextDouble();
            longitude *= MAXLONGITUDE;

            if(rGen.nextInt(3) == 1)
            {
                longitude = -longitude;
            }

            txtLongitude.setText(String.valueOf(longitude));


            String urlString = URLBASE + "lat=" + latitude + "&long=" + longitude + URLDATAFORMAT;

            GetGeoData getGeoData = new GetGeoData();
            getGeoData.execute(urlString);
        }
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

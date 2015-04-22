package bit.mcnear1.teleportgame;

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


public class MainActivity extends ActionBarActivity {

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
            double temp = rGen.nextDouble();
            temp *= MAXLATITUDE;

            if(rGen.nextBoolean())
            {
                latitude = -temp;
            }

            txtLatitude.setText(String.valueOf(latitude));

            temp = rGen.nextDouble();
            temp *= MAXLONGITUDE;

            if(rGen.nextBoolean())
            {
                longitude = -temp;
            }

            txtLongitude.setText(String.valueOf(longitude));

            GetGeoData getGeoData = new GetGeoData();
            getGeoData.execute();
        }
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
            }catch (JSONException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}

package bit.mcnear1.mywebservicetest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class login extends Activity {

    protected EditText txtUserName;
    protected EditText txtPassword;
    protected TextView txtTest;
    protected SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPrefs = getSharedPreferences("AlexTestApp", Context.MODE_PRIVATE);

        int id = sharedPrefs.getInt("userID", -1);
        String userName = sharedPrefs.getString("fullName", "Unknown");
        if(id != -1)
        {
            Intent dashboardIntent = new Intent(this, DashTest.class);
            startActivity(dashboardIntent);
        }

        txtUserName = (EditText)findViewById(R.id.txtUserName);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtTest = (TextView)findViewById(R.id.lblTest);

        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new BtnLoginClickHandler());
    }

    public class BtnLoginClickHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            UserInfoFetcher userInfoFetcher = new UserInfoFetcher();
            userInfoFetcher.execute("");
        }
    }

    public class UserInfoFetcher extends AsyncTask<String, Void, byte[]> {
        @Override
        protected byte[] doInBackground(String... urlString) {

            byte[] output = null;

            try {
                URL urlObject = new URL("http://alexandermcneill.nz/mobileapi/user.php");
                HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
                connection.setRequestMethod("POST");

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("userName", txtUserName.getText().toString()));
                params.add(new BasicNameValuePair("password", txtPassword.getText().toString()));

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();

                connection.connect();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
                    InputStream is = connection.getInputStream();

                    int byteCount = 0;
                    byte[] buffer = new byte[1024];

                    while ((byteCount = is.read(buffer)) > 0) {
                        dataOut.write(buffer);
                    }

                    dataOut.close();
                    output = dataOut.toByteArray();
                }

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return output;
        }


        @Override
        protected void onPostExecute(byte[] fetchedData) {

            try{
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONArray(jsonString).getJSONObject(0);

                txtTest.setText("Logged in as " + fetchedJson.getString("firstName") + fetchedJson.getString("lastName"));


                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putInt("userID", fetchedJson.getInt("id"));
                editor.putString("userName", txtUserName.getText().toString());
                editor.putString("fullName", fetchedJson.getString("firstName") + " " + fetchedJson.getString("lastName"));
                editor.apply();

                Intent dashboardIntent = new Intent(login.this, DashTest.class);
                startActivity(dashboardIntent);
            }catch (Exception ex)
            {
                txtTest.setText("Unable to login");
            }
        }
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}

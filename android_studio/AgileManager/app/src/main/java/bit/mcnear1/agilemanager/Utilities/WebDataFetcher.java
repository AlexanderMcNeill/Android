package bit.mcnear1.agilemanager.Utilities;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import org.apache.http.NameValuePair;
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
import java.util.List;

/**
 * Created by alexmcneill on 14/05/15.
 */
public class WebDataFetcher extends AsyncTask<String, Void, byte[]> {

    protected JSONObject params = null;

    public WebDataFetcher(JSONObject params)
    {
        this.params = params;
    }

    @Override
    protected byte[] doInBackground(String... urlString) {

        byte[] output = null;

        try {

            URL urlObject = new URL(urlString[0]);
            HttpURLConnection connection = (HttpURLConnection)urlObject.openConnection();

            if(params != null)
            {
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                String jsonString = params.toString();
                writer.write(jsonString);
                writer.flush();
                writer.close();
            }

            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                InputStream is = connection.getInputStream();
                ByteArrayOutputStream dataOut = new ByteArrayOutputStream();

                int byteCount = 0;
                byte[] buffer = new byte[1024];

                while ((byteCount = is.read(buffer)) > 0) {
                    dataOut.write(buffer);
                }

                dataOut.close();

                output = dataOut.toByteArray();
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
    protected void onPostExecute(byte[] fetchedData)
    {
        //To be added by child
    }
}

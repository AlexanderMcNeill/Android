package bit.mcnear1.teleportgame;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by alexmcneill on 28/04/15.
 */
public class WebDataFetcher extends AsyncTask<String, Void, byte[]> {
    @Override
    protected byte[] doInBackground(String... urlString) {

        byte[] output = null;

        try {
            URL urlObject = new URL(urlString[0]);
            HttpURLConnection connection = (HttpURLConnection)urlObject.openConnection();
            connection.connect();

            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
                InputStream is = connection.getInputStream();

                int byteCount = 0;
                byte[] buffer = new byte[1024];

                while ((byteCount = is.read(buffer)) > 0)
                {
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

        processData(fetchedData);
    }

    protected void processData(byte[] fetchedData)
    {
        //To be added by user
    }

}

package bit.mcnear1.agilemanager;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;

import org.apache.http.NameValuePair;

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

    protected List<NameValuePair> params;

    public WebDataFetcher(List<NameValuePair> params)
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

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
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

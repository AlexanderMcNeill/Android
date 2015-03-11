package com.bit.mcnear1.resourcespractice;

import android.support.v7.app.ActionBarActivity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class ResorcePacticeMain extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resorce_pactice_main);
        
        TextView txtDisplay = (TextView)findViewById(R.id.txtDisplay);
        
        Resources resourceResolver = getResources();
        
        int textColorCode = resourceResolver.getColor(R.color.blueForText);
        
        txtDisplay.setTextColor(textColorCode);
        
        String febFridays = "February Fridays on: ";
        int[] datesArray = resourceResolver.getIntArray(R.array.FebFridays);
        
        for(int i : datesArray)
        {
        	febFridays += i + " ";
        }
        
        txtDisplay.setText(febFridays);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.resorce_pactice_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

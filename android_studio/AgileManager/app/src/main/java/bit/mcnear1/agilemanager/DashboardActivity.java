package bit.mcnear1.agilemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

public class DashboardActivity extends NavigationActivity {

    protected SharedPreferences sharedPrefs;
    protected View page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);

        page = getLayoutInflater().inflate(R.layout.fragment_dashboard, null);
        pageContainer.addView(page);

        TextView lblFullName = (TextView)page.findViewById(R.id.lblFullname);
        lblFullName.setText(sharedPrefs.getString("firstName", "Unknown") + " " + sharedPrefs.getString("lastName", "Unknown"));
    }
}

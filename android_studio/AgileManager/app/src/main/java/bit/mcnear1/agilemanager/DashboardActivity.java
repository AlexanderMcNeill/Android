package bit.mcnear1.agilemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DashboardActivity extends ActionBarActivity {

    protected SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedPrefs = getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);

        TextView lblFullName = (TextView)findViewById(R.id.lblFullname);
        lblFullName.setText(sharedPrefs.getString("firstName", "Unknown") + " " + sharedPrefs.getString("lastName", "Unknown"));

        Button btnLogout = (Button)findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new BtnLogoutHandler());
    }


    public class BtnLogoutHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.remove("userID");
            editor.remove("userName");
            editor.remove("fullName");
            editor.apply();

            Intent loginIntent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        }
    }
}

package bit.mcnear1.mywebservicetest;

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


public class DashTest extends ActionBarActivity {

    protected TextView txtWelcomeBanner;
    protected SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_test);

        sharedPrefs = getSharedPreferences("AlexTestApp", Context.MODE_PRIVATE);
        txtWelcomeBanner = (TextView)findViewById(R.id.txtWelcomeBanner);

        txtWelcomeBanner.setText("Welcome " + sharedPrefs.getString("fullName", "Unknown User"));

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

            Intent loginIntent = new Intent(DashTest.this, login.class);
            startActivity(loginIntent);
        }
    }
}

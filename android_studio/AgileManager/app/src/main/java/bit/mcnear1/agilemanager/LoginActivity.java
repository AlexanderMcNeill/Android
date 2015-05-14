package bit.mcnear1.agilemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends Activity {

    protected EditText txtUserName;
    protected EditText txtPassword;
    protected TextView lblLoginResult;
    protected SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPrefs = getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);

        if(sharedPrefs.getInt("userID", -1) != -1)
        {
            Intent dashboardIntent = new Intent(this, DashboardActivity.class);
            startActivity(dashboardIntent);
        }

        linkXML();

    }

    protected void linkXML()
    {
        txtUserName = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        lblLoginResult = (TextView)findViewById(R.id.lblResult);
        TextView lblCreateAccount = (TextView)findViewById(R.id.lblCreateAccount);
        lblCreateAccount.setOnClickListener(new LblCreateAccountHandler());

        Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new BtnLoginHandler());
    }

    public class BtnLoginHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {

            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userName", txtUserName.getText().toString()));
            params.add(new BasicNameValuePair("password", txtPassword.getText().toString()));

            UserInfoFetcher userInfoFetcher = new UserInfoFetcher(params);
            userInfoFetcher.execute("http://alexandermcneill.nz/mobileapi/login.php");
        }
    }

    public class LblCreateAccountHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {

        }
    }

    public class UserInfoFetcher extends WebDataFetcher
    {
        public UserInfoFetcher(List<NameValuePair> params)
        {
            super(params);
        }

        @Override
        protected void processData(byte[] fetchedData)
        {
            try{
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);

                if(fetchedJson.getInt("success") == 1) {
                    lblLoginResult.setText("Logged in as " + fetchedJson.getString("firstName") + fetchedJson.getString("lastName"));


                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putInt("userID", fetchedJson.getInt("id"));
                    editor.putString("userName", txtUserName.getText().toString());
                    editor.putString("firstName", fetchedJson.getString("firstName"));
                    editor.putString("lastName", fetchedJson.getString("lastName"));
                    editor.apply();

                    Intent dashboardIntent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(dashboardIntent);
                }
                else
                {
                    lblLoginResult.setText(fetchedJson.getString("error"));
                }
            }catch (Exception ex)
            {
                ex.printStackTrace();
                lblLoginResult.setText("Unable to login");
            }
            finally {
                lblLoginResult.setVisibility(TextView.VISIBLE);
            }
        }
    }
}

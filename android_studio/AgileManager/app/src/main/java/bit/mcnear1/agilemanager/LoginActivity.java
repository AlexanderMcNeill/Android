package bit.mcnear1.agilemanager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginActivity extends ActionBarActivity {

    public static final String LOGIN_PREFERENCES = "AgileManagerApp";

    protected EditText txtUserName;
    protected EditText txtPassword;
    protected SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPrefs = getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);

        //Checking if the user is already logged in. If they are sending them to their dashboard
        if(sharedPrefs.getInt("userID", -1) != -1)
        {
            Intent dashboardIntent = new Intent(this, MainActivity.class);
            startActivity(dashboardIntent);
        }

        //Method that sets up all the screen elements described in the xml
        setupScreenElements();

    }

    //Overriding the back button to stop a user who has logged ou accidently going back in without logging in again
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    protected void setupScreenElements()
    {
        //Linking elements to their objects
        txtUserName = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);

        //Setting onclick listeners of the login and create account buttons
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
            Intent createAccountIntent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(createAccountIntent);
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
                    String loginMessage = "Logged in as " + fetchedJson.getString("firstName") + fetchedJson.getString("lastName");

                    Toast toast = Toast.makeText(LoginActivity.this, loginMessage, Toast.LENGTH_SHORT);
                    toast.show();

                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putInt("userID", fetchedJson.getInt("id"));
                    editor.putString("userName", txtUserName.getText().toString());
                    editor.putString("firstName", fetchedJson.getString("firstName"));
                    editor.putString("lastName", fetchedJson.getString("lastName"));
                    editor.apply();

                    Intent dashboardIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(dashboardIntent);
                }
                else
                {
                    String errorMessage = fetchedJson.getString("error");
                    Toast toast = Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }catch (Exception ex)
            {
                ex.printStackTrace();
                Toast toast = Toast.makeText(LoginActivity.this, "Unable to login", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}

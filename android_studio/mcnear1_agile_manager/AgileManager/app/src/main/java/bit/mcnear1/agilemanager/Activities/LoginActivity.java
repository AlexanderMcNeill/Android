package bit.mcnear1.agilemanager.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bit.mcnear1.agilemanager.R;
import bit.mcnear1.agilemanager.Utilities.WebDataFetcher;


public class LoginActivity extends ActionBarActivity {

    public final static String URLBASE = "http://alexandermcneill.nz:443/members/?";

    //Variables to link to the xml elements
    protected EditText txtUserName;
    protected EditText txtPassword;

    //Reference to the shared preferences for accessing the user info and other app data
    protected SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Getting a reference to the shared preferences for the app
        sharedPrefs = getSharedPreferences(getResources().getString(R.string.app_preferences), Context.MODE_PRIVATE);

        //Checking if the user is already logged in
        if(sharedPrefs.getInt("userID", -1) != -1)
        {
            //If the user is already logged redirecting them to the dashboard
            Intent mainApplicationIntent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainApplicationIntent);
        }

        //Method that sets up all the screen elements described in the xml
        setupScreenElements();

    }

    //Overriding the back button to stop a user who has logged ou accidentally going back in without logging in again
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //Setting up all the screen elements referenced in the xml
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

    //On click handler for when that login button is clicked
    public class BtnLoginHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {

            //Getting the info to send to the webservice ready
            String urlString = URLBASE +
                                "user_name=" +
                                txtUserName.getText().toString()+
                                "&password=" + txtPassword.getText();

            //Creating user info fetcher
            UserInfoFetcher userInfoFetcher = new UserInfoFetcher();
            userInfoFetcher.execute(urlString);
        }
    }

    //On click handler for when the create account button is clicked
    public class LblCreateAccountHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            //Sending them to the create account activity
            Intent createAccountIntent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(createAccountIntent);
        }
    }

    //Class that will be used to fetch the users data from the webservice
    public class UserInfoFetcher extends WebDataFetcher
    {
        public UserInfoFetcher()
        {
            super(null);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData)
        {
            try{
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);

                JSONObject response_json = fetchedJson.getJSONObject("member_request_response");

                //Checking that there was a user matching their input credentials
                if(response_json.getInt("response_code") == 0) {

                    JSONObject userJson = fetchedJson.getJSONObject("user");

                    //Informing the user they are logged in with a toast
                    String loginMessage = "Logged in as " + userJson.getString("firstName") + userJson.getString("lastName");

                    Toast toast = Toast.makeText(LoginActivity.this, loginMessage, Toast.LENGTH_SHORT);
                    toast.show();

                    //Putting the users info into the apps preferences
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putInt("userID", userJson.getInt("id"));
                    editor.putString("userName", userJson.getString("firstName"));
                    editor.putString("firstName", userJson.getString("firstName"));
                    editor.putString("lastName", userJson.getString("lastName"));
                    editor.apply();

                    //Opening the dashboard
                    Intent mainApplicationIntent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(mainApplicationIntent);
                }
                else
                {
                    //Displaying the error message to the user
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

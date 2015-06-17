package bit.mcnear1.agilemanager.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bit.mcnear1.agilemanager.R;
import bit.mcnear1.agilemanager.Utilities.WebDataFetcher;


public class CreateAccountActivity extends ActionBarActivity {

    public final static String URLBASE = "http://alexandermcneill.nz:443/members/";
    protected EditText txtUserName;
    protected EditText txtPassword;
    protected EditText txtRePassword;
    protected EditText txtFirstName;
    protected EditText txtLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //Setting up the create account buttons click handler
        Button btnCreateAccount = (Button)findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new BtnCreateAccountHandler());

        linkXML();
    }

    //Method that links all of the xml elements to their objects
    protected void linkXML()
    {
        txtUserName = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtRePassword = (EditText)findViewById(R.id.txtRePassword);
        txtFirstName = (EditText)findViewById(R.id.txtFirstName);
        txtLastName = (EditText)findViewById(R.id.txtLastName);
    }

    //Class that handles the create account button click
    public class BtnCreateAccountHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {

            //Getting the text from each of the create account fields
            String firstName= txtFirstName.getText().toString();
            String lastName= txtLastName.getText().toString();
            String username= txtUserName.getText().toString();
            String password= txtPassword.getText().toString();
            String rePassword= txtRePassword.getText().toString();

            //Checking id the user input is valid
            boolean validUserInfo = checkValidUserInfo(firstName, lastName, username, password, rePassword);

            if(validUserInfo) {
                try {
                    //Creating a json object for the add user request
                    JSONObject newUserRequest = new JSONObject();
                    newUserRequest.put("function", "add_user");
                    newUserRequest.put("first_name", firstName);
                    newUserRequest.put("last_name", lastName);
                    newUserRequest.put("username", username);
                    newUserRequest.put("password", password);

                    //Running the request
                    CreateAccountRequest createAccountRequest = new CreateAccountRequest(newUserRequest);
                    createAccountRequest.execute(URLBASE);

                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }

        //Method that checks if the user filled out the create account fields correctly
        public boolean checkValidUserInfo(String firstName, String lastName, String username, String password, String rePassword)
        {
            boolean valid = true;
            String errorMessage = "";

            //Checking that each of the fields is filled
            if(firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                valid = false;
                errorMessage = "Please fill out all fields";
            }

            //Making sure that the passwords match
            if(!password.equals(rePassword))
            {
                valid = false;
                errorMessage = "Passwords don't match";
            }

            //If invalid displaying why to the user
            if(!valid) {
                Toast toast = Toast.makeText(CreateAccountActivity.this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }

            return valid;
        }
    }

    //Class for requesting the webservice to create an account
    public class CreateAccountRequest extends WebDataFetcher
    {
        public CreateAccountRequest(JSONObject params)
        {
            super(params);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData)
        {
            try{

                //Getting the response data
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);
                JSONObject response_json = fetchedJson.getJSONObject("new_user_response");

                //Creating a toast for displaying the result of the rquest
                Toast toast;

                //Checking that the request came back ok
                if(response_json.getInt("response_code") == 0) {
                    //Displaying the success to the user
                    toast = Toast.makeText(CreateAccountActivity.this, response_json.getString("response_message"), Toast.LENGTH_SHORT);
                    toast.show();

                    //Switching the page back to the login page
                    Intent loginIntent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
                else
                {
                    //Displaying why the request failed
                    toast = Toast.makeText(CreateAccountActivity.this, response_json.getString("response_message"), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            catch (JSONException ex)
            {
                //Displaying to the user that something went wrong
                ex.printStackTrace();
                Toast toast = Toast.makeText(CreateAccountActivity.this, "Error fetching data", Toast.LENGTH_SHORT);
                toast.show();
            }
            catch (NullPointerException ex)
            {
                //Displaying to the user that something went wrong
                ex.printStackTrace();
                Toast toast = Toast.makeText(CreateAccountActivity.this, "Error fetching data", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}

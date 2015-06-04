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
    protected EditText txtFirstName;
    protected EditText txtLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button btnCreateAccount = (Button)findViewById(R.id.btnCreateAccount);
        btnCreateAccount.setOnClickListener(new BtnCreateAccountHandler());

        linkXML();
    }

    protected void linkXML()
    {
        txtUserName = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        txtFirstName = (EditText)findViewById(R.id.txtFirstName);
        txtLastName = (EditText)findViewById(R.id.txtLastName);
    }

    public class BtnCreateAccountHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {

            try {
                JSONObject newUserRequest = new JSONObject();
                newUserRequest.put("function", "add_user");
                newUserRequest.put("first_name", txtFirstName.getText().toString());
                newUserRequest.put("last_name", txtLastName.getText().toString());
                newUserRequest.put("username", txtUserName.getText().toString());
                newUserRequest.put("password", txtPassword.getText().toString());

                CreateAccountRequest createAccountRequest = new CreateAccountRequest(newUserRequest);
                createAccountRequest.execute(URLBASE);
            }catch (JSONException ex)
            {
                ex.printStackTrace();
            }
        }
    }

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
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);
                JSONObject response_json = fetchedJson.getJSONObject("new_user_response");
                Toast toast;

                if(response_json.getInt("response_code") == 0) {
                    toast = Toast.makeText(CreateAccountActivity.this, response_json.getString("response_message"), Toast.LENGTH_SHORT);
                    toast.show();

                    Intent loginIntent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
                else
                {
                    toast = Toast.makeText(CreateAccountActivity.this, response_json.getString("response_message"), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
                Toast toast = Toast.makeText(CreateAccountActivity.this, "Error fetching data", Toast.LENGTH_SHORT);
                toast.show();
            }
            catch (NullPointerException ex)
            {
                ex.printStackTrace();
                Toast toast = Toast.makeText(CreateAccountActivity.this, "Error fetching data", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}

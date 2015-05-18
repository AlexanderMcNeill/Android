package bit.mcnear1.agilemanager;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class CreateAccountActivity extends ActionBarActivity {

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
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("userName", txtUserName.getText().toString()));
            params.add(new BasicNameValuePair("password", txtPassword.getText().toString()));
            params.add(new BasicNameValuePair("firstName", txtFirstName.getText().toString()));
            params.add(new BasicNameValuePair("lastName", txtLastName.getText().toString()));

            CreateAccountRequest createAccountRequest = new CreateAccountRequest(params);
            createAccountRequest.execute("http://alexandermcneill.nz/mobileapi/addUser.php");
        }
    }

    public class CreateAccountRequest extends WebDataFetcher
    {
        public CreateAccountRequest(List<NameValuePair> params)
        {
            super(params);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData)
        {
            try{
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);

                Toast toast;

                if(fetchedJson.getInt("success") == 1) {
                    toast = Toast.makeText(CreateAccountActivity.this, fetchedJson.getString("message"), Toast.LENGTH_SHORT);
                    toast.show();

                    Intent loginIntent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                }
                else
                {
                    toast = Toast.makeText(CreateAccountActivity.this, fetchedJson.getString("error"), Toast.LENGTH_SHORT);
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

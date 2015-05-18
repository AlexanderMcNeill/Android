package bit.mcnear1.agilemanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DashboardActivity extends NavigationActivity {

    protected SharedPreferences sharedPrefs;
    protected View page;
    protected LinearLayout recentScrumContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);

        page = getLayoutInflater().inflate(R.layout.fragment_dashboard, null);
        pageContainer.addView(page);

        TextView lblFullName = (TextView)page.findViewById(R.id.lblFullname);
        lblFullName.setText(sharedPrefs.getString("firstName", "Unknown") + " " + sharedPrefs.getString("lastName", "Unknown"));

        recentScrumContainer = (LinearLayout)findViewById(R.id.recentScrumContainer);

        FetchRecentScrums fetchRecentScrums = new FetchRecentScrums(null);
        fetchRecentScrums.execute("http://alexandermcneill.nz/mobileapi/userRecentScrums.php");
    }

    protected void addRecentScrum(String teamName, JSONArray teamGoals, JSONArray userGoals)
    {
        View newRecentScrumView = getLayoutInflater().inflate(R.layout.dashboard_item, null);
        TextView lblTeamName = (TextView)newRecentScrumView.findViewById(R.id.lblScrumHeader);
        lblTeamName.setText(teamName);
        lblTeamName.setOnClickListener(new ScrumTitleClickHandler(newRecentScrumView));

        TextView lblTeamGoals = (TextView)newRecentScrumView.findViewById(R.id.lblTeamGoals);
        StringBuilder teamGoalsSB = new StringBuilder();
        for(int i = 0; i < teamGoals.length(); i++)
        {
            try {
                teamGoalsSB.append(teamGoals.getString(i));
                teamGoalsSB.append("\n");
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
            }
        }
        lblTeamGoals.setText(teamGoalsSB.toString());

        TextView lblUserGoals = (TextView)newRecentScrumView.findViewById(R.id.lblUserGoals);
        StringBuilder userGoalsSB = new StringBuilder();
        for(int i = 0; i < userGoals.length(); i++)
        {
            try {
                userGoalsSB.append(userGoals.getString(i));
                userGoalsSB.append("\n");
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
            }
        }
        lblUserGoals.setText(userGoalsSB.toString());

        recentScrumContainer.addView(newRecentScrumView);
    }

    public class ScrumTitleClickHandler implements View.OnClickListener
    {
        private View mMainContainer;

        public ScrumTitleClickHandler(View mainContainer)
        {
            mMainContainer = mainContainer;
        }

        @Override
        public void onClick(View view) {
            View scrumInfoContainer = mMainContainer.findViewById(R.id.scrumInfoContainer);

            if(scrumInfoContainer.getVisibility() == View.VISIBLE)
            {
                scrumInfoContainer.setVisibility(View.GONE);
            }
            else
            {
                scrumInfoContainer.setVisibility(View.VISIBLE);
            }
        }
    }

    public class FetchRecentScrums extends WebDataFetcher
    {

        public FetchRecentScrums(List<NameValuePair> params) {
            super(params);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData)
        {
            try{
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);

                if(fetchedJson.getInt("success") == 1) {

                    JSONArray scrumMeetings = fetchedJson.getJSONArray("scrums");
                    for(int i = 0; i < scrumMeetings.length(); i++)
                    {
                        JSONObject teamJson = scrumMeetings.getJSONObject(i);
                        JSONArray teamGoals = teamJson.getJSONArray("teamGoals");
                        JSONArray userGoals = teamJson.getJSONArray("userGoals");
                        String teamName = teamJson.getString("teamName");

                        addRecentScrum(teamName, teamGoals, userGoals);
                    }
                }
                else
                {
                    String errorMessage = fetchedJson.getString("error");
                    Toast toast = Toast.makeText(DashboardActivity.this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }catch (Exception ex)
            {
                ex.printStackTrace();
                Toast toast = Toast.makeText(DashboardActivity.this, "Error", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}

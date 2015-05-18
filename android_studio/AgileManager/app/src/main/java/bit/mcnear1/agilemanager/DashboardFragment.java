package bit.mcnear1.agilemanager;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by alexmcneill on 18/05/15.
 */
public class DashboardFragment extends Fragment {
    protected LinearLayout recentScrumContainer;
    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflator.inflate(R.layout.fragment_dashboard, container, false);
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);

        TextView lblFullName = (TextView)v.findViewById(R.id.lblFullname);
        lblFullName.setText(sharedPrefs.getString("firstName", "Unknown") + " " + sharedPrefs.getString("lastName", "Unknown"));

        recentScrumContainer = (LinearLayout)v.findViewById(R.id.recentScrumContainer);

        FetchRecentScrums fetchRecentScrums = new FetchRecentScrums(null);
        fetchRecentScrums.execute("http://alexandermcneill.nz/mobileapi/userRecentScrums.php");
        return v;
    }

    protected void addRecentScrum(String teamName, JSONArray teamGoals, JSONArray userGoals)
    {

        View newRecentScrumView = getActivity().getLayoutInflater().inflate(R.layout.dashboard_item, null);

        TextView lblTeamName = (TextView)newRecentScrumView.findViewById(R.id.lblScrumHeader);
        lblTeamName.setText(teamName);
        lblTeamName.setOnClickListener(new ScrumTitleClickHandler(newRecentScrumView));

        TextView lblTeamGoals = (TextView)newRecentScrumView.findViewById(R.id.lblTeamGoals);
        String teamGoalsString = createGoalBulletPoints(teamGoals);
        lblTeamGoals.setText(teamGoalsString);

        TextView lblUserGoals = (TextView)newRecentScrumView.findViewById(R.id.lblUserGoals);
        String userGoalsString = createGoalBulletPoints(userGoals);
        lblUserGoals.setText(userGoalsString);

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

    public String createGoalBulletPoints(JSONArray goals)
    {
        StringBuilder goalsStringBuilder = new StringBuilder();
        for(int i = 0; i < goals.length(); i++)
        {
            try {
                goalsStringBuilder.append("-");
                goalsStringBuilder.append(goals.getString(i));
                goalsStringBuilder.append("\n");
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
            }
        }

        return goalsStringBuilder.toString();
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
                    Toast toast = Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }catch (Exception ex)
            {
                ex.printStackTrace();
                Toast toast = Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}

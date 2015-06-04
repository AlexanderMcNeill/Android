package bit.mcnear1.agilemanager.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bit.mcnear1.agilemanager.R;

/**
 * Created by alexmcneill on 17/05/15.
 */
public class RecentScrumFragment extends Fragment {

    protected TextView mLblTeamName;
    protected LinearLayout mScrumDetailsContainer;
    protected JSONObject mTeamJson;

    @Override
    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflator.inflate(R.layout.fragment_recent_scrum_item, container, false);

        Bundle args = getArguments();
        String teamJsonString = args.getString("teamJsonString");

        try {
            mTeamJson = new JSONObject(teamJsonString);

            setupXMLElements(v);
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
            Log.d("Convert Failed", "Failed to convert recent scrum json string");
        }

        return v;
    }

    protected void setupXMLElements(View v)
    {
        try {
            //Getting the team name label
            mLblTeamName = (TextView)v.findViewById(R.id.lblScrumHeader);
            //Setting it to the team name from the json
            mLblTeamName.setText(mTeamJson.getString("team_name"));
            //Setting the onclick handler
            mLblTeamName.setOnClickListener(new ScrumTitleClickHandler());
            //Getting the scrum details container. Will be used to make it toggle on and off later
            mScrumDetailsContainer = (LinearLayout)v.findViewById(R.id.scrumInfoContainer);

            if(mTeamJson.has("most_recent_scrum"))
            {
                JSONObject scrumJson = mTeamJson.getJSONObject("most_recent_scrum");

                //Filling that team goals with the data from the json object
                TextView lblTeamGoals = (TextView)v.findViewById(R.id.lblTeamGoals);
                String teamGoalsString = createGoalBulletPoints(scrumJson.getJSONArray("goals"));
                lblTeamGoals.setText(teamGoalsString);

                TextView lblDate = (TextView)v.findViewById(R.id.lblDate);
                lblDate.setText("Date: " + scrumJson.getString("date"));
            }
        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
            Log.d("Read Failed", "Failed to read recent scrum data");
        }
    }

    //Method that converts json string array into a string that bullet points each of the strings
    public String createGoalBulletPoints(JSONArray goals)
    {
        StringBuilder goalsStringBuilder = new StringBuilder();

        try {
            //Looping through each of the goals in the json array
            for(int i = 0; i < goals.length(); i++)
            {
                    goalsStringBuilder.append("-");
                    goalsStringBuilder.append(goals.getString(i));
                    goalsStringBuilder.append("\n");
            }

        }
        catch (JSONException ex)
        {
            ex.printStackTrace();
            return "Error fetching goals";
        }

        return goalsStringBuilder.toString();
    }

    public class ScrumTitleClickHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            if(mScrumDetailsContainer.getVisibility() == View.VISIBLE)
            {
                mScrumDetailsContainer.setVisibility(View.GONE);
            }
            else
            {
                mScrumDetailsContainer.setVisibility(View.VISIBLE);
            }
        }
    }
}

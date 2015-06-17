package bit.mcnear1.agilemanager.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bit.mcnear1.agilemanager.R;
import bit.mcnear1.agilemanager.SwappablePage;
import bit.mcnear1.agilemanager.Utilities.WebDataFetcher;

/**
 * Created by alexmcneill on 11/06/15.
 */
public class JoinTeamFragment extends Fragment{

    protected int userID;
    protected Spinner teamSpinner;
    protected String[] teamNames;
    protected int[] teamIDs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Join Team");

        View v = inflater.inflate(R.layout.fragment_join_team, container, false);

        //Getting the users id from the shared preferences
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);
        userID = sharedPrefs.getInt("userID",-1);

        setupScreenElements(v);

        return v;
    }

    protected void setupScreenElements(View v)
    {
        //Setting the join team buttons on click handler
        Button btnJoinTeam = (Button) v.findViewById(R.id.btnJoinTeam);
        btnJoinTeam.setOnClickListener(new BtnJoinTeamHandler());

        teamSpinner = (Spinner) v.findViewById(R.id.teamSpinner);
        GetTeamsRequest getTeamsRequest = new GetTeamsRequest();
        getTeamsRequest.execute("http://alexandermcneill.nz:443/teams");
    }

    public class BtnJoinTeamHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {

            try {
                JSONObject requestJson = new JSONObject();

                requestJson.put("team_id", teamIDs[teamSpinner.getSelectedItemPosition()]);
                requestJson.put("user_id", userID);
                requestJson.put("function", "join_team");

                JoinTeamRequest joinTeamRequest = new JoinTeamRequest(requestJson);
                joinTeamRequest.execute("http://alexandermcneill.nz:443/members/");
            }catch (JSONException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public class JoinTeamRequest extends WebDataFetcher {

        public JoinTeamRequest(JSONObject params) {
            super(params);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData) {
            try {
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);
                JSONObject joinTeamResponse = fetchedJson.getJSONObject("join_team_response");
                Toast toast;

                if (joinTeamResponse.getInt("response_code") == 0) {
                    toast = Toast.makeText(getActivity(), joinTeamResponse.getString("response_message"), Toast.LENGTH_SHORT);
                    toast.show();
                    SwappablePage swappablePageActivity = (SwappablePage)getActivity();
                    swappablePageActivity.updateCurrentPage(new DashboardFragment());
                }
                else
                {
                    toast = Toast.makeText(getActivity(), "Unable to join team", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }catch (JSONException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public class GetTeamsRequest extends WebDataFetcher {

        public GetTeamsRequest() {
            super(null);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData) {
            try {
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);
                JSONObject getTeamsResponse = fetchedJson.getJSONObject("get_team_response");

                if(getTeamsResponse.getInt("response_code") == 0)
                {
                    JSONArray teams = fetchedJson.getJSONArray("teams");
                    teamNames = new String[teams.length()];
                    teamIDs = new int[teams.length()];

                    for(int i = 0; i < teams.length(); i++)
                    {
                        JSONObject team = teams.getJSONObject(i);
                        teamNames[i] = team.getString("name");
                        teamIDs[i] = team.getInt("id");
                    }

                    ArrayAdapter<String> teamNameAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, teamNames);
                    teamNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    teamSpinner.setAdapter(teamNameAdapter);
                }
            }catch (JSONException ex)
            {
                ex.printStackTrace();
            }
        }
    }
}

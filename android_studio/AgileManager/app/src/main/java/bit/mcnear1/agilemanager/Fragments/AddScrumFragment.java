package bit.mcnear1.agilemanager.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bit.mcnear1.agilemanager.R;
import bit.mcnear1.agilemanager.Utilities.WebDataFetcher;

/**
 * Created by alexmcneill on 18/05/15.
 */
public class AddScrumFragment extends Fragment {

    protected EditText txtComment;
    protected Spinner spinnerTeam;
    protected ListView listGoals;
    protected String[] teamNames;
    protected int[] teamIds;
    protected ArrayList<String> goals;
    protected SharedPreferences sharedPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_scrum, container, false);

        if (savedInstanceState != null)
        {
            goals = savedInstanceState.getStringArrayList("goals");
        }
        else
        {
            goals = new ArrayList<>();
        }

        sharedPrefs = getActivity().getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);

        setupScreenElements(v);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList("goals", goals);
        super.onSaveInstanceState(outState);
    }

    public void setupScreenElements(View v)
    {
        //Setting up buttons click handlers
        Button btnAddGoal = (Button)v.findViewById(R.id.btnAddGoal);
        btnAddGoal.setOnClickListener(new BtnAddGoalHandler());

        Button btnAddScrum = (Button)v.findViewById(R.id.btnAddScrum);
        btnAddScrum.setOnClickListener(new BtnAddScrumHandler());

        //Making edit text for the comment available to the whole class
        txtComment = (EditText)v.findViewById(R.id.txtScrumComment);

        //Setting up spinner for holding team names
        spinnerTeam = (Spinner)v.findViewById(R.id.spinnerTeam);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        String userID = String.valueOf(sharedPrefs.getInt("userID", -1));
        params.add(new BasicNameValuePair("userID", userID));
        TeamFetch teamFetch = new TeamFetch(params);
        teamFetch.execute("http://alexandermcneill.nz/mobileapi/getUserTeams.php");

        //Setting up goal list view contain the list of goals
        listGoals = (ListView)v.findViewById(R.id.listGoal);

        ArrayAdapter<String> goalAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, goals);
        listGoals.setAdapter(goalAdapter);
    }

    public class BtnAddGoalHandler implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            // get prompts.xml view
            LayoutInflater li = LayoutInflater.from(getActivity());
            View promptsView = li.inflate(R.layout.add_goal_prompt, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView.findViewById(R.id.txtNewGoal);

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // get user input and set it to result
                                    // edit text
                                    goals.add(userInput.getText().toString());
                                }
                            })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    dialog.cancel();
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }

    public class BtnAddScrumHandler implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {

            int teamID = teamIds[spinnerTeam.getSelectedItemPosition()];
            int userID = sharedPrefs.getInt("userID", -1);
            String[] goals = null;
            String comment = null;
        }
    }

    public class TeamFetch extends WebDataFetcher
    {

        public TeamFetch(List<NameValuePair> params) {
            super(params);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData) {
            String jsonString = new String(fetchedData);

            try {
                JSONObject fetchedJson = new JSONObject(jsonString);

                if(fetchedJson.getInt("success") == 1) {

                    JSONArray teamJsonArray = fetchedJson.getJSONArray("teams");

                    teamNames = new String[teamJsonArray.length()];
                    teamIds = new int[teamJsonArray.length()];

                    for (int i = 0; i < teamJsonArray.length(); i++) {
                        JSONObject teamJson = teamJsonArray.getJSONObject(i);
                        teamNames[i] = teamJson.getString("name");
                        teamIds[i] = teamJson.getInt("id");
                    }

                    ArrayAdapter<String> teamNameAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, teamNames);
                    teamNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerTeam.setAdapter(teamNameAdapter);

                }
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public class AddTeamRequest extends WebDataFetcher
    {

        public AddTeamRequest(List<NameValuePair> params) {
            super(params);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData) {

        }
    }

}

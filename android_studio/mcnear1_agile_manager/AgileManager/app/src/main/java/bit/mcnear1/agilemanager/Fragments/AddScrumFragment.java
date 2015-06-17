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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import bit.mcnear1.agilemanager.R;
import bit.mcnear1.agilemanager.SwappablePage;
import bit.mcnear1.agilemanager.Utilities.WebDataFetcher;

/**
 * Created by alexmcneill on 18/05/15.
 */
public class AddScrumFragment extends Fragment {

    public final static String USERINFOURLBASE = "http://alexandermcneill.nz:443/members/?";
    public final static String ADDSCRUMURLBASE = "http://alexandermcneill.nz:443/scrums/";
    protected EditText txtComment;
    protected Spinner spinnerTeam;
    protected ListView listGoals;
    protected String[] teamNames;
    protected int[] teamIds;
    protected ArrayList<String> goals;
    protected SharedPreferences sharedPrefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle("Add Scrum");

        View v = inflater.inflate(R.layout.fragment_add_scrum, container, false);

        //Checking if the page is opening of the first time
        if (savedInstanceState == null)
        {
            //If its the first time opening the page creating the goals list
            goals = new ArrayList<>();
        }
        else
        {
            //If its recovering from a rotate or other state change getting the goals from the saved instance state
            goals = savedInstanceState.getStringArrayList("goals");
        }

        //Getting the shared preferences for the app
        sharedPrefs = getActivity().getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);

        setupScreenElements(v);

        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //Saving goals before state change
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

        TeamFetch teamFetch = new TeamFetch();
        String urlString = USERINFOURLBASE + "user_id=" + sharedPrefs.getInt("userID",-1);
        teamFetch.execute(urlString);

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

            //Getting the selected team id
            int teamID = teamIds[spinnerTeam.getSelectedItemPosition()];

            try {
                //Creating a json object for the add scrum request and handing it all the needed scrum data
                JSONObject newScrumRequest = new JSONObject();

                newScrumRequest.put("function", "add_scrum");
                newScrumRequest.put("team_id", teamID);
                newScrumRequest.put("comment", txtComment.getText().toString());

                //Converting the goals list into a json array to give to the request
                JSONArray goalsJsonArray = new JSONArray(goals);
                newScrumRequest.put("goals", goalsJsonArray);

                //Making the request
                AddScrumRequest addTeamRequest = new AddScrumRequest(newScrumRequest);
                addTeamRequest.execute(ADDSCRUMURLBASE);
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    //Class used for fetching the users teams from the webservice
    public class TeamFetch extends WebDataFetcher
    {

        public TeamFetch() {
            super(null);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData) {

            try {
                //Getting the data from the request
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);

                //Checking the request worked
                JSONObject response_json = fetchedJson.getJSONObject("member_request_response");
                if(response_json.getInt("response_code") == 0) {
                    //Getting teams array from the user object
                    JSONObject userDataJson = fetchedJson.getJSONObject("user");
                    JSONArray teamJsonArray = userDataJson.getJSONArray("teams");

                    //Setting up the arrays to hold the team ids and names
                    teamNames = new String[teamJsonArray.length()];
                    teamIds = new int[teamJsonArray.length()];

                    //Adding each teams info to the team ids and team names
                    for (int i = 0; i < teamJsonArray.length(); i++) {
                        JSONObject teamJson = teamJsonArray.getJSONObject(i);
                        teamNames[i] = teamJson.getString("team_name");
                        teamIds[i] = teamJson.getInt("team_id");
                    }

                    //Creating an array adapter for displaying the teams
                    ArrayAdapter<String> teamNameAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, teamNames);
                    teamNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                    //Assigning the team name adapter to the team selection spinner
                    spinnerTeam.setAdapter(teamNameAdapter);

                }
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public class AddScrumRequest extends WebDataFetcher
    {

        public AddScrumRequest(JSONObject params) {
            super(params);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData) {
            try{
                //Getting the response data
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);
                JSONObject response_json = fetchedJson.getJSONObject("add_scrum_response");

                //Creating a toast for showing the result to the user
                Toast toast;

                //Checking if the request was a success
                if(response_json.getInt("response_code") == 0) {
                    //Showing the response message
                    toast = Toast.makeText(getActivity(), response_json.getString("response_message"), Toast.LENGTH_SHORT);
                    toast.show();

                    //Getting the created scrums id
                    final int newScrumID = fetchedJson.getInt("scrum_id");


                    //Creating a alert to give the user the option of joining the scrum straight away
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

                    alertDialogBuilder.setMessage("Would you like to join the new scrum?");

                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            SwappablePage swappablePageActivity = (SwappablePage)getActivity();
                                            JoinScrumFragment joinScrumFragment = new JoinScrumFragment();

                                            //Creating a bundle to give it the scrums id
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("scrum_id", newScrumID);

                                            //Passing the data to the new fragment
                                            joinScrumFragment.setArguments(bundle);

                                            swappablePageActivity.updateCurrentPage(joinScrumFragment);
                                        }
                                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            SwappablePage swappablePageActivity = (SwappablePage)getActivity();
                                            ViewScrumFragment viewScrumFragment = new ViewScrumFragment();

                                            //Creating a bundle to give it the scrums id
                                            Bundle bundle = new Bundle();
                                            bundle.putInt("scrum_id", newScrumID);

                                            //Passing the data to the new fragment
                                            viewScrumFragment.setArguments(bundle);

                                            swappablePageActivity.updateCurrentPage(viewScrumFragment);
                                        }
                                    });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
                else
                {
                    //Showing the user why the request failed
                    toast = Toast.makeText(getActivity(), response_json.getString("response_message"), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            catch (JSONException ex)
            {
                //Showing the user that a error occurred
                ex.printStackTrace();
                Toast toast = Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}

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

            JSONObject newScrumRequest = new JSONObject();
            int teamID = teamIds[spinnerTeam.getSelectedItemPosition()];

            try {
                newScrumRequest.put("function", "add_scrum");
                newScrumRequest.put("team_id", teamID);
                newScrumRequest.put("comment", txtComment.getText().toString());

                JSONArray goalsJsonArray = new JSONArray(goals);

                newScrumRequest.put("goals", goalsJsonArray);

                AddScrumRequest addTeamRequest = new AddScrumRequest(newScrumRequest);
                addTeamRequest.execute(ADDSCRUMURLBASE);
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    public class TeamFetch extends WebDataFetcher
    {

        public TeamFetch() {
            super(null);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData) {
            String jsonString = new String(fetchedData);

            try {
                JSONObject fetchedJson = new JSONObject(jsonString);

                JSONObject response_json = fetchedJson.getJSONObject("member_request_response");
                if(response_json.getInt("response_code") == 0) {
                    JSONObject userDataJson = fetchedJson.getJSONObject("user");
                    JSONArray teamJsonArray = userDataJson.getJSONArray("teams");

                    teamNames = new String[teamJsonArray.length()];
                    teamIds = new int[teamJsonArray.length()];

                    for (int i = 0; i < teamJsonArray.length(); i++) {
                        JSONObject teamJson = teamJsonArray.getJSONObject(i);
                        teamNames[i] = teamJson.getString("team_name");
                        teamIds[i] = teamJson.getInt("team_id");
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

    public class AddScrumRequest extends WebDataFetcher
    {

        public AddScrumRequest(JSONObject params) {
            super(params);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData) {
            try{
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);
                final JSONObject response_json = fetchedJson.getJSONObject("add_scrum_response");
                Toast toast;

                if(response_json.getInt("response_code") == 0) {
                    toast = Toast.makeText(getActivity(), response_json.getString("response_message"), Toast.LENGTH_SHORT);
                    toast.show();
                    final int newScrumID = fetchedJson.getInt("scrum_id");


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
                    toast = Toast.makeText(getActivity(), response_json.getString("response_message"), Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
                Toast toast = Toast.makeText(getActivity(), "Error fetching data", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}

package bit.mcnear1.agilemanager.Fragments;

import android.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import bit.mcnear1.agilemanager.R;
import bit.mcnear1.agilemanager.SwappablePage;
import bit.mcnear1.agilemanager.Utilities.WebDataFetcher;

/**
 * Created by alexmcneill on 27/05/15.
 */
public class JoinScrumFragment extends Fragment{
    public final static String URLBASE = "http://alexandermcneill.nz:443/scrums/";

    protected ArrayList<String> goals;
    protected EditText txtConstraints;
    protected ListView listGoals;
    protected int userID;
    protected int scrumID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_join_scrum, container, false);

        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("AgileManagerApp", Context.MODE_PRIVATE);
        userID = sharedPrefs.getInt("userID",-1);
        Bundle args = getArguments();
        scrumID = args.getInt("scrum_id");
        if (savedInstanceState != null)
        {
            goals = savedInstanceState.getStringArrayList("goals");
        }
        else
        {
            goals = new ArrayList<>();
        }

        setupScreenElements(v);


        return v;
    }

    protected void setupScreenElements(View v)
    {
        Button btnAddGoal = (Button)v.findViewById(R.id.btnAddGoal);
        btnAddGoal.setOnClickListener(new BtnAddGoalHandler());
        Button btnJoinScrum = (Button)v.findViewById(R.id.btnJoinScrum);
        btnJoinScrum.setOnClickListener(new BtnJoinScrumHandelr());

        txtConstraints = (EditText)v.findViewById(R.id.txtConstraints);

        //Setting up goal list view contain the list of goals
        listGoals = (ListView)v.findViewById(R.id.listGoal);

        ArrayAdapter<String> goalAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, goals);
        listGoals.setAdapter(goalAdapter);
    }

    public class BtnJoinScrumHandelr implements View.OnClickListener
    {

        @Override
        public void onClick(View view) {
            try {
                JSONObject joinScrumData = new JSONObject();
                joinScrumData.put("function", "join_scrum");
                joinScrumData.put("user_id", userID);
                joinScrumData.put("scrum_id", scrumID);
                joinScrumData.put("obstacles", txtConstraints.getText().toString());

                JSONArray goalData = new JSONArray();
                for(int i = 0; i < goals.size(); i++)
                {
                    goalData.put(goals.get(i));
                }

                joinScrumData.put("goals", goalData);
                JoinScrumRequest joinScrumRequest = new JoinScrumRequest(joinScrumData);
                joinScrumRequest.execute(URLBASE);
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
            }

        }
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

    public class JoinScrumRequest extends WebDataFetcher {

        public JoinScrumRequest(JSONObject params) {
            super(params);
        }

        @Override
        protected void onPostExecute(byte[] fetchedData) {
            try {
                String jsonString = new String(fetchedData);
                JSONObject fetchedJson = new JSONObject(jsonString);
                final JSONObject response_json = fetchedJson.getJSONObject("add_scrum_response");
                Toast toast;

                if (response_json.getInt("response_code") == 0) {
                    toast = Toast.makeText(getActivity(), response_json.getString("response_message"), Toast.LENGTH_SHORT);
                    toast.show();
                }


            }catch (JSONException ex)
            {
                ex.printStackTrace();
            }
            SwappablePage swappablePageActivity = (SwappablePage)getActivity();
            ViewScrumFragment viewScrumFragment = new ViewScrumFragment();

            //Creating a bundle to give it the scrums id
            Bundle bundle = new Bundle();
            bundle.putInt("scrum_id", scrumID);

            //Passing the data to the new fragment
            viewScrumFragment.setArguments(bundle);

            swappablePageActivity.updateCurrentPage(viewScrumFragment);
        }
    }
}
